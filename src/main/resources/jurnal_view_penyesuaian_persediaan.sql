create or replace view jurnal_view_penyesuaian_persediaan as
select 
t.id,
t.tanggal,
t.keterangan,
t.jenis,
t.created_at,
'' as faktur
from penyesuaian_persediaan p
inner join transaksi t on t.id = p.id;

create or replace view jurnal_view_penyesuaian_persediaan_details as
select * from (
    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = pd.rekening_id) rekening_id,
    iif(td1.jumlah < 0,(select s.total_jual + s.total_beli from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id), 0) as debet,
    iif(td1.jumlah < 0, 0, (select s.total_jual + s.total_beli from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id)) as kredit,
    t.id as jurnal_view_id
    from penyesuaian_persediaan p
    inner join transaksi t on t.id = p.id
    inner join penyesuaian_persediaan_details pd on pd.penyesuaian_persediaan_id = p.id
    inner join transaksi_details1 td1 on td1.id = pd.id

    union all

    select 
    MD5(NOW()::TEXT||RANDOM()::TEXT) as id,
    (select r.id from rekening r where r.id = (select pr.rek_inventory_id from persediaan pr where pr.id = td1.barang_id)) rekening_id,
    iif(td1.jumlah < 0, 0, (select s.total_jual + s.total_beli from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id)) as debet,
    iif(td1.jumlah < 0, (select s.total_jual + s.total_beli from stok s where pd.id = s.transaksi_details_id and t.id = s.transaksi_id), 0) as kredit,
    t.id as jurnal_view_id
    from penyesuaian_persediaan p
    inner join transaksi t on t.id = p.id
    inner join penyesuaian_persediaan_details pd on pd.penyesuaian_persediaan_id = p.id
    inner join transaksi_details1 td1 on td1.id = pd.id
) a
order by a.jurnal_view_id;