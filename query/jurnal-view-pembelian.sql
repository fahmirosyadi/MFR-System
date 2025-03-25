drop view if exists jurnal_view_pembelian;
drop view if exists jurnal_view_pembelian_details;

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
    getTotalBersihPD(pd.id) as kredit,
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
order by a.jurnal_view_id
