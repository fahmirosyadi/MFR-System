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