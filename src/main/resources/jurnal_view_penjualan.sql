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
    (select r.id from rekening r where r.id = (select rek_penjualan_id from account_link)) rekening_id,
    get_td_total_bersih(pd.id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id

    union all

    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_potongan_penjualan_id from account_link)) rekening_id,
    get_td_dis(pd.id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_penjualan_id from persediaan per where per.id = td1.barang_id)) as rekening_id,
    0 as debet,
    get_td_total(pd.id) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    inner join transaksi_details1 td1 on td1.id = pd.id
    
    union all
    select
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select pj.rek_keluaran_id from pajak pj where pj.id = td.pajak_id)) rekening_id,
    0 as debet,
    get_td_pajak(pd.id) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    inner join transaksi_details td on td.id = pd.id
    where td.pajak_id is not null
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_pembayaran_piutang_id from account_link)) rekening_id,
    (p.bayar) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_penjualan_id from account_link)) rekening_id,
    0 as debet,
    p.bayar as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_hpp_id from persediaan per where per.id = td1.barang_id)) as rekening_id,
    (select s.total_jual from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    inner join transaksi_details1 td1 on td1.id = pd.id
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_inventory_id from persediaan per where per.id = td1.barang_id)) as rekening_id,
    0 as debet,
    (select s.total_jual from stok s where td1.created_at = s.created_at and t.id = s.transaksi_id) as kredit,
    t.id as jurnal_view_id
    from penjualan p
    inner join transaksi t on t.id = p.id
    inner join penjualan_details pd on pd.penjualan_id = p.id
    inner join transaksi_details1 td1 on td1.id = pd.id

) a
where a.debet != 0 or a.kredit != 0
order by a.jurnal_view_id;