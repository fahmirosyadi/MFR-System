create or replace view jurnal_view_pembayaran_utang as
select 
t.id,
t.tanggal,
t.keterangan,
t.jenis,
t.created_at,
'' as faktur
from pembayaran_utang p
inner join transaksi t on t.id = p.id;

create or replace view jurnal_view_pembayaran_utang_details as
select * from (
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select al.rek_pembayaran_utang_id from account_link al)) rekening_id,
    0 as debet,
    (pd.bayar) as kredit,
    t.id as jurnal_view_id
    from pembayaran_utang p
    inner join transaksi t on t.id = p.id
    inner join pembayaran_utang_details pd on pd.pembayaran_utang_id = p.id

    union all
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select al.rek_pembelian_id from account_link al)) rekening_id,
    pd.bayar as debet,
    0 as kredit,
    t.id as jurnal_view_id
    from pembayaran_utang p
    inner join transaksi t on t.id = p.id
    inner join pembayaran_utang_details pd on pd.pembayaran_utang_id = p.id
) a
order by a.jurnal_view_id;