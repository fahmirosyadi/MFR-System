drop table if exists jurnal_view;
drop table if exists jurnal_view_details;
drop view if exists jurnal_view;
drop view if exists jurnal_view_details;

drop view if exists jurnal_view_penjualan;
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

drop view if exists jurnal_view_penjualan_details;
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
order by a.jurnal_view_id
