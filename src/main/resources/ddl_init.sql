drop view if exists jurnal_view cascade;
drop view if exists jurnal_view_details cascade;
drop view if exists jurnal_view_barang_masuk cascade;
drop view if exists jurnal_view_barang_masuk_details cascade;
drop view if exists jurnal_view_pembayaran_utang cascade;
drop view if exists jurnal_view_pembayaran_piutang cascade;
drop view if exists jurnal_view_pembayaran_utang_details cascade;
drop view if exists jurnal_view_pembayaran_piutang_details cascade;
drop view if exists stok_tamp cascade;
drop view if exists stok cascade;
drop function if exists get_table;

DROP FUNCTION IF EXISTS iif cascade;
CREATE OR REPLACE FUNCTION public.iif(condition boolean, true_result numeric, false_result numeric)
 RETURNS numeric
 LANGUAGE plpgsql
AS '
BEGIN
    IF condition THEN 
        RETURN true_result;
    END IF;
    RETURN false_result;
END
';

drop function if exists get_td_total cascade;
CREATE OR REPLACE FUNCTION public.get_td_total(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    (select td1.jumlah * td.harga from transaksi_details td inner join transaksi_details1 td1 on td1.id = td.id 
    where td.id = id1);
end;
';

drop function if exists get_td_dis cascade;
CREATE OR REPLACE FUNCTION public.get_td_dis(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    (select get_td_total(id1) * td.diskon / 100 from transaksi_details td 
    where td.id = id1);
end;
';

drop function if exists get_td_total_after_dis cascade;
CREATE OR REPLACE FUNCTION public.get_td_total_after_dis(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    get_td_total(id1) - get_td_dis(id1);
end;
';

drop function if exists get_td_pajak cascade;
CREATE OR REPLACE FUNCTION public.get_td_pajak(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    get_td_total_after_dis(id1) * 
    coalesce((select pj.persen from pajak pj where pj.id = (select pd.pajak_id from transaksi_details pd where pd.id = id1)), 0) / 100;
end;
';

drop function if exists get_td_total_bersih cascade;
CREATE OR REPLACE FUNCTION public.get_td_total_bersih(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    get_td_total_after_dis(id1) + get_td_pajak(id1);
end;
';

drop function if exists get_t_total cascade;
CREATE OR REPLACE FUNCTION public.get_t_total(id1 numeric) RETURNS double precision LANGUAGE plpgsql
AS '
begin
return
    (select sum(get_td_total(td1.id)) from transaksi_details1 td1 where td1.transaksi_id = id1);
end;
';

drop function if exists get_t_diskon cascade;
CREATE OR REPLACE FUNCTION public.get_t_diskon(id1 numeric) RETURNS double precision LANGUAGE plpgsql
AS '
begin
return
    (select sum(get_td_dis(td1.id)) from transaksi_details1 td1 where td1.transaksi_id = id1);
end;
';

drop function if exists get_t_pajak cascade;
CREATE OR REPLACE FUNCTION public.get_t_pajak(id1 numeric)
 RETURNS double precision
 LANGUAGE plpgsql
AS '
begin
return
    (select sum(get_td_pajak(td1.id)) from transaksi_details1 td1 where td1.transaksi_id = id1);
end;
';

DROP FUNCTION IF EXISTS get_total_stok cascade;
-- CREATE OR REPLACE FUNCTION public.get_total_stok(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN (coalesce((select sum(pmd.jumlah * pmd.harga) from pembelian_details pmd 
--     where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0) - GET_TOTAL_PENJUALAN(tanggal));
-- END
-- ';

DROP FUNCTION IF EXISTS get_total_saldo cascade;
-- CREATE OR REPLACE FUNCTION public.get_total_saldo(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN (coalesce((select sum(pmd.jumlah * pmd.harga) from pembelian_details pmd where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0));
-- END
-- ';

DROP FUNCTION IF EXISTS get_total_penjualan cascade;
-- CREATE OR REPLACE FUNCTION public.get_total_penjualan(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN CEILING(GET_TOTAL_PEMBELIAN(tanggal) / GET_QTY_STOK(tanggal)) * (select pd.jumlah from penjualan_details pd 
--     where (select t.tanggal from transaksi t where t.id = pd.penjualan_id) = tanggal);
-- END
-- ';

DROP FUNCTION IF EXISTS get_total_pembelian cascade;
-- CREATE OR REPLACE FUNCTION public.get_total_pembelian(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN (coalesce((select sum(pmd.jumlah * pmd.harga) from pembelian_details pmd 
--     where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0));
-- END
-- ';

DROP FUNCTION IF EXISTS get_qty_stok cascade;
-- CREATE OR REPLACE FUNCTION public.get_qty_stok(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN (coalesce((select sum(pmd.jumlah) from pembelian_details pmd 
--     where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0))
--     -
--     (coalesce((select sum(pnd.jumlah) from penjualan_details pnd 
--     where (select t.tanggal from transaksi t where t.id = pnd.penjualan_id) < tanggal),0));
-- END
-- ';

DROP FUNCTION IF EXISTS get_qty_pembelian cascade;
-- CREATE OR REPLACE FUNCTION public.get_qty_pembelian(tanggal date)
--  RETURNS numeric
--  LANGUAGE plpgsql
-- AS '
-- BEGIN
--     RETURN (coalesce((select sum(pmd.jumlah) from pembelian_details pmd 
--     where (select t.tanggal from transaksi t where t.id = pmd.pembelian_id) < tanggal),0));
-- END
-- ';

