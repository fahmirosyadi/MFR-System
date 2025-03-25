create view jurnal_view as
select 
t.id,
t.tanggal,
t.keterangan,
t.jenis,
t.created_at,
'' as faktur
from jurnal p
inner join transaksi t on t.id = p.id
union all select * from jurnal_view_pembelian
union all select * from jurnal_view_penjualan
union all select * from jurnal_view_penyesuaian_persediaan
union all select * from jurnal_view_pembayaran_utang
union all select * from jurnal_view_pembayaran_piutang;

create view jurnal_view_details as
select 
MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
jd.rekening_id, jd.debet, jd.kredit, jd.jurnal_id as jurnal_view_id 
from jurnal_details jd
union all select * from jurnal_view_pembelian_details
union all select * from jurnal_view_penjualan_details
union all select * from jurnal_view_penyesuaian_persediaan_details
union all select * from jurnal_view_pembayaran_utang_details
union all select * from jurnal_view_pembayaran_piutang_details;