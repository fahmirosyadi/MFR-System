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
    (select r.id from rekening r where r.id = (select rek_pembelian_id from account_link)) rekening_id,
    0 as debet,
    get_td_total_bersih(pd.id) as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    
    union all 
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_potongan_pembelian_id from account_link)) rekening_id,
    0 as debet,
    get_td_dis(pd.id) as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    inner join transaksi_details td on td.id = pd.id
    where td.diskon is not null and td.diskon > 0

    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select per.rek_inventory_id from persediaan per where per.id = td1.barang_id)) as rekening_id,
    get_td_total(pd.id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    inner join transaksi_details td on td.id = pd.id
    inner join transaksi_details1 td1 on td1.id = td.id

    union all
    select
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select pj.rek_masukkan_id from pajak pj where pj.id = td.pajak_id)) rekening_id,
    get_td_pajak(pd.id) as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    inner join pembelian_details pd on pd.pembelian_id = p.id
    inner join transaksi_details td on td.id = pd.id
    where td.pajak_id is not null
    
    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_pembayaran_utang_id from account_link)) rekening_id,
    0 as debet,
    (p.bayar) as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    where p.bayar != 0

    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select rek_pembelian_id from account_link)) rekening_id,
    p.bayar as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembelian p
    inner join transaksi t on t.id = p.id
    where p.bayar != 0
) a
order by a.jurnal_view_id;