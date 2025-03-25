drop view if exists jurnal_view;
create view jurnal_view as

select * from jurnal_view_pembelian
union all select * from jurnal_view_penjualan;

drop view if exists jurnal_view_details;
create view jurnal_view_details as

select * from jurnal_view_pembelian_details
union all select * from jurnal_view_penjualan_details;
