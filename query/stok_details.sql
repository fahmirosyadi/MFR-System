CREATE OR REPLACE FUNCTION GET_TOTAL_STOK(tanggal date)
RETURNS numeric
LANGUAGE plpgsql
AS '
BEGIN
    RETURN (coalesce((select sum(pmd.jumlah * pmd.harga) from pembelian_details pmd 
    where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0) - GET_TOTAL_PENJUALAN(tanggal));
END
'

CREATE OR REPLACE FUNCTION GET_TOTAL_PENJUALAN(tanggal date)
RETURNS numeric
LANGUAGE plpgsql
AS '
BEGIN
    RETURN CEILING(GET_TOTAL_PEMBELIAN(tanggal) / GET_QTY_STOK(tanggal)) * (select pd.jumlah from penjualan_details pd 
    where (select t.tanggal from transaksi t where t.id = pd.penjualan_id) = tanggal);
END
'

CREATE OR REPLACE FUNCTION GET_QTY_STOK(tanggal date)
RETURNS numeric
LANGUAGE plpgsql
AS '
BEGIN
    RETURN (coalesce((select sum(pmd.jumlah) from pembelian_details pmd 
    where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0))
    -
    (coalesce((select sum(pnd.jumlah) from penjualan_details pnd 
    where (select t.tanggal from transaksi t where t.id = pnd.penjualan_id) < tanggal),0));
END
'

CREATE OR REPLACE FUNCTION GET_STOK()
RETURNS table(c1 character varying, c2 character varying)

AS '
BEGIN
    RETURN SELECT ''1'',''2'';
END
'
LANGUAGE plpgsql

--drop view if exists stok_details;
--create view stok_details as

select *
from 
(
    select
    concat('pm-',p.id,'-',pm.id) as id,
    t.tanggal,
    pm.faktur,
    'Pembelian' as keterangan,
    'pembelian' as jenis,
    p.barang, 
    pd.jumlah as qty_beli, 
    pd.harga as harga_beli, 
    (pd.jumlah * pd.harga) as total_beli,
    0 as qty_jual, 
    0 as harga_jual, 
    0 as total_jual,
    GET_QTY_STOK(t.tanggal) + pd.jumlah as qty,
    CEILING((GET_TOTAL_PEMBELIAN(t.tanggal) + (pd.jumlah * pd.harga)) / (GET_QTY_STOK(t.tanggal) + pd.jumlah)) as harga,
    GET_TOTAL_PEMBELIAN(t.tanggal) + (pd.jumlah * pd.harga) as total,
    t.created_at,
    p.id as persediaan_id
    
    from persediaan p
    inner join pembelian_details pd on pd.barang_id = p.id
    inner join pembelian pm on pm.id = pd.pembelian_id
    inner join transaksi t on t.id = pm.id
    
    union all
    select 
    concat('pn-',p.id,'-',pn.id) as id,
    t.tanggal,
    pn.faktur,
    'Penjualan' as keterangan,
    'penjualan' as jenis,
    p.barang, 
    0 as qty_beli, 
    0 as harga_beli, 
    0 as total_beli,
    pd.jumlah as qty_jual, 
    CEILING(GET_TOTAL_PEMBELIAN(t.tanggal) / GET_QTY_PEMBELIAN(t.tanggal)) as harga_jual, 
    CEILING(GET_TOTAL_PEMBELIAN(t.tanggal) / GET_QTY_PEMBELIAN(t.tanggal)) * pd.jumlah as total_jual,
    GET_QTY_STOK(t.tanggal) - pd.jumlah as qty,
    CEILING(GET_TOTAL_PEMBELIAN(t.tanggal) / (GET_QTY_STOK(t.tanggal) - pd.jumlah)) as harga,
    GET_TOTAL_PEMBELIAN(t.tanggal) as total,
    t.created_at,
    p.id as persediaan_id
    from persediaan p
    inner join penjualan_details pd on pd.barang_id = p.id
    inner join penjualan pn on pn.id = pd.penjualan_id
    inner join transaksi t on t.id = pn.id
    order by tanggal
)
s 
