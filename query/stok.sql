drop view if exists stok_tamp;
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
order by tanggal
