create or replace view stok_tamp as
select 
concat('pm-',p.id,'-',pm.id) as id,
t.tanggal,
pd.jumlah as qty_beli, 
pd.harga as harga_beli, 
0 as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join pembelian_details pd on pd.barang_id = p.id
inner join pembelian pm on pm.id = pd.pembelian_id
inner join transaksi t on t.id = pm.id
union all
select 
concat('pn-',p.id,'-',pn.id) as id,
t.tanggal,
0 as qty_beli, 
0 as harga_beli, 
pd.jumlah as qty_jual,
t.id as transaksi_id,
p.id as persediaan_id
,pd.id as transaksi_details_id
from persediaan p
inner join penjualan_details pd on pd.barang_id = p.id
inner join penjualan pn on pn.id = pd.penjualan_id
inner join transaksi t on t.id = pn.id
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

qty_1 integer default 0;
total_1 integer default 0;

i integer default 0;

begin 
    for r in 
    select * from stok_tamp
    loop
       id[i] = r.id;
       transaksi_details_id[i] = r.transaksi_details_id;
       transaksi_id[i] = r.transaksi_id;
       persediaan_id[i] = r.persediaan_id;
       qty_beli[i] = r.qty_beli;
       harga_beli[i] = r.harga_beli;
       total_beli[i] = qty_beli[i] * harga_beli[i];
       qty_jual[i] = r.qty_jual;
       harga_jual[i] = iif(qty_jual[i] > 0, harga[i - 1], 0);
       total_jual[i] = coalesce((qty_jual[i] * harga_jual[i]),0);
       qty_1 = qty_1 + qty_beli[i] - qty_jual[i];
       total_1 = total_1 + total_beli[i] - total_jual[i];
       qty[i] = qty_1;
       total[i] = total_1;
       harga[i] = case when qty[i] != 0 then ceiling(total[i] / qty[i])::integer else 0 end;
       i = i + 1;      
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


create or replace view jurnal_view_penjualan as
select 
t.id,
t.tanggal,
t.keterangan,
t.jenis,
t.created_at,
p.faktur
from penjualan p
inner join transaksi t on t.id = p.id;

create view jurnal_view_penjualan_details as
select * from (
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_penjualan_id) rekening_id,
    ((pd.jumlah * pd.harga) + (select pj.persen from pajak pj where pj.id = pd.pajak_id) * (pd.jumlah * pd.harga) / 100) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_penjualan_id from persediaan per where per.id = pd.barang_id)) as rekening_id,
    0 as debet,
    (pd.jumlah * pd.harga) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select pj.rek_keluaran_id from pajak pj where pj.id = pd.pajak_id)) rekening_id,
    0 as debet,
    ((select pj.persen from pajak pj where pj.id = pd.pajak_id) * (pd.jumlah * pd.harga) / 100) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_pelunasan_piutang_id) rekening_id,
    (p.bayar) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_penjualan_id) rekening_id,
    0 as debet,
    p.bayar as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_hpp_id from persediaan per where per.id = pd.barang_id)) as rekening_id,
    (select s.total_jual from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_inventory_id from persediaan per where per.id = pd.barang_id)) as rekening_id,
    0 as debet,
    (select s.total_jual from stok s where pd.created_at = s.created_at and t.id = s.transaksi_id) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
) a

where a.debet != 0 or a.kredit != 0
order by a.jurnal_view_id;


create or replace view jurnal_view_pembelian as
select 
t.id,
t.tanggal,
t.keterangan,
t.jenis,
t.created_at,
p.faktur
from pembelian p
inner join transaksi t on t.id = p.id;

create or replace view jurnal_view_pembelian_details as
select * from (
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_pembelian_id) rekening_id,
    0 as debet,
    get_total_bersih_pd(pd.id) as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_inventory_id from persediaan per where per.id = pd.barang_id)) as rekening_id,
    (pd.jumlah * pd.harga) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
    union all
    select
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select pj.rek_masukkan_id from pajak pj where pj.id = pd.pajak_id)) rekening_id,
    ((select pj.persen from pajak pj where pj.id = pd.pajak_id) * (pd.jumlah * pd.harga) / 100) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_pelunasan_utang_id) rekening_id,
    0 as debet,
    (p.bayar) as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = p.rek_pembelian_id) rekening_id,
    p.bayar as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
) a

where a.debet != 0 or a.kredit != 0
order by a.jurnal_view_id;

create view jurnal_view as
select * from jurnal_view_pembelian
union all select * from jurnal_view_penjualan;

create view jurnal_view_details as
select * from jurnal_view_pembelian_details
union all select * from jurnal_view_penjualan_details;