drop table if exists stok_details;
drop view if exists jurnal_view;
drop view if exists jurnal_view_details;
drop view if exists jurnal_view_penjualan;
drop view if exists jurnal_view_penjualan_details;
drop view if exists stok;
drop function if exists get_table;
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
       harga[i] = ceiling(total[i] / qty[i]);
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

drop view if exists stok;
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

