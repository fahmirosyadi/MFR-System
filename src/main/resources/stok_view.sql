create or replace view stok_tamp as

select 
concat('pm-',p.id,'-',pm.id) as id,
t.tanggal,
td1.jumlah as qty_beli, 
td.harga as harga_beli, 
0 as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join transaksi_details1 td1 on td1.barang_id = p.id
inner join transaksi_details td on td.id = td1.id
inner join pembelian_details pd on pd.id = td.id
inner join pembelian pm on pm.id = pd.pembelian_id
inner join transaksi t on t.id = pm.id

union all

select 
concat('pn-',p.id,'-',pn.id) as id,
t.tanggal,
0 as qty_beli, 
0 as harga_beli, 
td1.jumlah as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join transaksi_details1 td1 on td1.barang_id = p.id
inner join transaksi_details td on td.id = td1.id
inner join penjualan_details pd on pd.id = td.id
inner join penjualan pn on pn.id = pd.penjualan_id
inner join transaksi t on t.id = pn.id

union all

select 
concat('bms-',p.id,'-',pm.id) as id,
t.tanggal,
td1.jumlah as qty_beli, 
(select sum(jd.debet) - sum(jd.kredit) from jurnal_details jd where jd.rekening_id = pd.rekening_id) / td1.jumlah as harga_beli,
0 as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join transaksi_details1 td1 on td1.barang_id = p.id
inner join penyesuaian_persediaan_details pd on pd.id = td1.id
inner join penyesuaian_persediaan pm on pm.id = pd.penyesuaian_persediaan_id
inner join transaksi t on t.id = pm.id
where td1.jumlah > 0

union all 

select 
concat('bk-',p.id,'-',pn.id) as id,
t.tanggal,
0 as qty_beli, 
0 as harga_beli, 
td1.jumlah * -1 as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join transaksi_details1 td1 on td1.barang_id = p.id
inner join penyesuaian_persediaan_details pd on pd.id = td1.id
inner join penyesuaian_persediaan pn on pn.id = pd.penyesuaian_persediaan_id
inner join transaksi t on t.id = pn.id
where td1.jumlah < 0

order by tanggal;

create or replace function get_table()
returns table(
    id character varying(20), 
    qty_beli integer,
    harga_beli integer,
    total_beli integer,
    qty_jual integer,
    harga_jual integer,
    total_jual integer,
    qty integer,
    harga integer,
    total integer,
    transaksi_id integer,
    transaksi_details_id integer,
    persediaan_id integer
) language 'plpgsql'
as
'
declare 

r stok_tamp%rowtype;

id character varying(20)[];
transaksi_details_id integer[];
transaksi_id integer[];
persediaan_id integer[];

qty_beli integer[];
harga_beli integer[];
total_beli integer[];

qty_jual integer[];
harga_jual integer[];
total_jual integer[];

qty integer[];
harga integer[];
total integer[];

qty_1 integer[];
total_1 integer[];
harga_1 integer[];
total_dalam_proses integer default 0;

i integer default 0;

persediaan_id_before integer;

begin 
    for r in 
    select * from stok_tamp order by tanggal
    loop

        id[i] = r.id;
        transaksi_details_id[i] = r.transaksi_details_id;
        transaksi_id[i] = r.transaksi_id;
        persediaan_id[i] = r.persediaan_id;
        qty_jual[i] = r.qty_jual;
        harga_jual[i] = iif(qty_jual[i] > 0, coalesce(harga_1[r.persediaan_id], 0), 0);

        if(r.id like ''%bk%'') then
            total_dalam_proses = total_dalam_proses + coalesce((qty_jual[i] * harga_jual[i]),0);
        end if;

        qty_beli[i] = r.qty_beli;
        harga_beli[i] = iif(r.id like ''%bms%'', (case when qty_beli[i] != 0 then (coalesce(r.harga_beli, 0) + total_dalam_proses) / qty_beli[i] else 0 end)::numeric, r.harga_beli::numeric);
        total_beli[i] = qty_beli[i] * harga_beli[i];
        total_jual[i] = coalesce((qty_jual[i] * harga_jual[i]),0);
        qty_1[r.persediaan_id] = coalesce(qty_1[r.persediaan_id], 0) + qty_beli[i] - qty_jual[i];
        total_1[r.persediaan_id] = coalesce(total_1[r.persediaan_id], 0) + total_beli[i] - total_jual[i];
        harga_1[r.persediaan_id] =  case when qty_1[r.persediaan_id] != 0 then ceiling(total_1[r.persediaan_id] / qty_1[r.persediaan_id])::integer else 0 end;
        qty[i] = qty_1[r.persediaan_id];
        total[i] = total_1[r.persediaan_id];
        harga[i] = harga_1[r.persediaan_id];

        i = i + 1;
        persediaan_id_before = r.persediaan_id;      
    end loop;
    return query 
    select 
    unnest(id), 
    unnest(qty_beli), 
    unnest(harga_beli), 
    unnest(total_beli),
    unnest(qty_jual), 
    unnest(harga_jual), 
    unnest(total_jual),
    unnest(qty),
    unnest(harga),
    unnest(total),
    unnest(transaksi_id),
    unnest(transaksi_details_id),
    unnest(persediaan_id);
end;
';

create or replace view stok as
select 
t.tanggal,
t.created_at, 
t.keterangan, 
t.jenis,
coalesce(
    (select p.faktur from pembelian p where p.id = t.id), 
    (select p.faktur from penjualan p where p.id = t.id)
) as faktur,
s.*  
from get_table() s
inner join transaksi t on t.id = s.transaksi_id;