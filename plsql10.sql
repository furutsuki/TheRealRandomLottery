CREATE OR REPLACE PROCEDURE "PLKL010"
( out_para           OUT NUMBER
) AUTHID CURRENT_USER AS
 var_ins             VARCHAR2(32767):='';
 var_sql             VARCHAR2(32767):='';
 var_select          VARCHAR2(4096):='';
 tojitsu             VARCHAR2(8):='';  -- 当日
 togetsu             VARCHAR2(6):='';  -- 当月
 zengetsu            VARCHAR2(6):='';  -- 前月
 shm_before          VARCHAR2(32767):='';  -- 締め前システム運用企業コードリスト
 shm_after           VARCHAR2(32767):='';  -- 締め後システム運用企業コードリスト
 kanjo_list_before          VARCHAR2(32767):='';
 kanjo_list_after           VARCHAR2(32767):='';
 creattab VARCHAR2(32767):='';
 kanjo_list           VARCHAR2(32767):='';
 urg_dk_list          VARCHAR2(32767):='';
 urg_srk_list           VARCHAR2(32767):='';
 rec                 VARCHAR2(10):='';
 gosa_cnt            NUMBER(10):=0;
 ret                 NUMBER(10):=0;
--**********************************************************************************
-- FUNCTION UNION_ALL
--システム運用企業マスタと経費予算テーブルを結合して実績を取得する
--パラメータ「num」は経費予算テーブルに横持している実績金額を可変に取得する為の値で
--１～１２となる。
--**********************************************************************************
 FUNCTION UNION_ALL(shmLst VARCHAR2, num NUMBER) RETURN VARCHAR2 IS
 BEGIN
  var_ins := '';
  var_ins := var_ins || '   SELECT K.SYSTM_UNYO_KIGYO_CODE, N.KSSNZ,K.KANRI_KK_NENDO';
  var_ins := var_ins || '   ,TO_CHAR(ADD_MONTHS(TO_DATE(K.KANRI_KK_NENDO || N.KSSNZ, ''YYYYMM''), + ' || num || '), ''YYYYMM'') AS KEIRI_JM_NGSTU ';
  var_ins := var_ins || '   ,SUM(DECODE(M.GL_KNJKM_BNR_KUBUN, ''4'' , NVL(K.KANRI_KK_KNGK_TSUKI_' || num || ',0) * -1, NVL(K.KANRI_KK_KNGK_TSUKI_' || num || ',0))) AS KANRI_KK_KNGK_TSUKI';
  var_ins := var_ins || '   ,K.GROUP_CODE';
  var_ins := var_ins || '   ,K.KANRI_KK_KNJKM';
  var_ins := var_ins || '   FROM T_KEIHIYOSAN K,';
  var_ins := var_ins || '        M_KKKMKSHUYAKU M,';
  var_ins := var_ins || '   (SELECT SYSTM_UNYO_KIGYO_CODE,DECODE(KSSNZ, 12, TO_NUMBER(TO_CHAR(TO_DATE(''' || tojitsu || '''), ''YYYY'')), TO_NUMBER(TO_CHAR(TO_DATE(''' || tojitsu || '''), ''YYYY'')) - ';
  var_ins := var_ins || '     CASE WHEN TO_NUMBER(TO_CHAR(TO_DATE(''' || tojitsu || '''), ''MM'')) <= KSSNZ THEN 1 ELSE 0 END ) AS NEN ,KSSNZ';
  var_ins := var_ins || '   FROM M_KKSYSTEMUNYOKIGYOMST';
  var_ins := var_ins || ' 	WHERE ''' || tojitsu || ''' BETWEEN KKMST_YUKO_KIGEN_KSHB AND KKMST_YUKO_KIGEN_SHRYB';
  var_ins := var_ins || '   AND SHIYO_KNSH_KUBUN = ''0'' ';
  var_ins := var_ins || '   ) N ';
  var_ins := var_ins || '   WHERE K.SYSTM_UNYO_KIGYO_CODE = N.SYSTM_UNYO_KIGYO_CODE ';
  var_ins := var_ins || '   AND K.KANRI_KK_KNJKM = M.KANRI_KK_KNJKM ';
  var_ins := var_ins || '   AND M.KANRI_KK_UCHWK_CODE = ''            ''';
  var_ins := var_ins || '   AND M.SHIYO_KNSH_KUBUN = ''0'' ';
  var_ins := var_ins || '   AND K.KANRI_KK_NENDO IN (TO_NUMBER(N.NEN)-1, N.NEN)';
  var_ins := var_ins || '   AND K.KANRI_KK_TRK_KUBUN = ''4''';
  var_ins := var_ins || '   AND K.SYSTM_UNYO_KIGYO_CODE IN (' || shmLst || ')';
  var_ins := var_ins || '   AND K.TRK_PRGRM_ID NOT IN ( ''BTKG0018  '', ''BTKG0123  '' ) ';
  var_ins := var_ins || '   AND K.RDC_FDC_KEIHI_HAIFU_KUBUN = ''4'' ';
  var_ins := var_ins || '   GROUP BY K.SYSTM_UNYO_KIGYO_CODE, N.KSSNZ,K.KANRI_KK_NENDO,K.GROUP_CODE';
  var_ins := var_ins || '   ,K.KANRI_KK_KNJKM';
  var_ins := var_ins || '   ,TO_CHAR(ADD_MONTHS(TO_DATE(K.KANRI_KK_NENDO || N.KSSNZ, ''YYYYMM''), + ' || num || '), ''YYYYMM'')';
 RETURN var_ins;
 END;

--**********************************************************************************
-- FUNCTION SQL_INSERT
--残高チェック結果にＩＮＳＥＲＴするＳＱＬを生成する
--パラメータ「tmp_keihiyosan」は締前と締後で一時テーブルを分けている為、
--一時テーブル名を指定しているTMP_KEIHIYOSAN_BEFROE、TMP_KEIHIYOSAN_AFTER
--**********************************************************************************
 FUNCTION SQL_INSERT(shmLst VARCHAR2, hzk VARCHAR2, chkSyubetu VARCHAR2, dk_list VARCHAR2, srk_list VARCHAR2, tmp_keihiyosan VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  var_ins := ' INSERT INTO T_ZANCHECKKEKKA ';
  var_ins := var_ins || ' SELECT';
  var_ins := var_ins || '   ''11''';
  var_ins := var_ins || '   ,''ＰＬ累計額チェック''';
  var_ins := var_ins || '   , ''' || chkSyubetu || '''';
  var_ins := var_ins || '   , ''' || tojitsu || '''';
  var_ins := var_ins || '   ,''' || hzk || '''';
  var_ins := var_ins || '   ,A.SYSTM_UNYO_KIGYO_CODE';
  var_ins := var_ins || '   ,A.GROUP_CODE';
  var_ins := var_ins || '   ,C.GROUP_MEI_TNSHK_KANJI';
  var_ins := var_ins || '   ,A.KANJO_KMK_CODE';
  var_ins := var_ins || '   ,CASE A.KANJO_KMK_CODE';
  var_ins := var_ins || '    WHEN ''7020110'' THEN ''売上高''';
  var_ins := var_ins || '    WHEN ''7200110'' THEN ''売上総利益''';
  var_ins := var_ins || '    END AS KANJO_KMK_MEI';
  var_ins := var_ins || '   ,NULL';
  var_ins := var_ins || '   ,''0000000''';
  var_ins := var_ins || '   ,NULL';
  var_ins := var_ins || '   ,''ＧＬＯＶＩＡ''';
  var_ins := var_ins || '   ,''T_ZANCHKGLOVIAZAN''';
  var_ins := var_ins || '   ,NVL(A.GL_KIKAN_SRBZAN,0) - NVL(A.GL_KIKAN_KSZENJZAN,0) AS MOTO';
  var_ins := var_ins || '   ,''管理会計''';
  var_ins := var_ins || '   ,''T_KEIHIYOSAN''';
  var_ins := var_ins || '   ,NVL(B.KINGAKU,0)';
  var_ins := var_ins || '   ,(NVL(A.GL_KIKAN_SRBZAN,0) - NVL(A.GL_KIKAN_KSZENJZAN,0)) - NVL(B.KINGAKU,0) AS GOSAKGK';
  var_ins := var_ins || '   ,''PLKL010''';
  var_ins := var_ins || '   ,A.SYSTM_UNYO_KIGYO_CODE';
  var_ins := var_ins || '   ,NULL';
  var_ins := var_ins || '   ,''PLKL010''';
  var_ins := var_ins || '   ,A.SYSTM_UNYO_KIGYO_CODE';
  var_ins := var_ins || '   ,NULL';
  var_ins := var_ins || '   ,1';
  var_ins := var_ins || '   ,TO_CHAR(sysdate, ''YYYYMMDDHH24MISS'')';
  var_ins := var_ins || '   ,TO_CHAR(sysdate, ''YYYYMMDDHH24MISS'')';
  var_ins := var_ins || '   FROM';
  var_ins := var_ins || ' 	(SELECT ';
  var_ins := var_ins || ' 	W.SYSTM_UNYO_KIGYO_CODE,W.GROUP_CODE';
  var_ins := var_ins || ' 	,W.KANJO_KMK_CODE';
  var_ins := var_ins || ' 	,SUM(NVL(W.GL_KIKAN_KSZENJZAN,0)) AS GL_KIKAN_KSZENJZAN';
  var_ins := var_ins || ' 	,SUM(NVL(W.GL_KIKAN_SRBZAN,0)) AS GL_KIKAN_SRBZAN';
  var_ins := var_ins || ' 	FROM';
  var_ins := var_ins || ' 		(SELECT T.SYSTM_UNYO_KIGYO_CODE';
  var_ins := var_ins || ' 			,NVL(MAX(M.GROUP_CODE), ''XXXX'') AS GROUP_CODE';
  var_ins := var_ins || '          ,''7020110'' AS KANJO_KMK_CODE';
  var_ins := var_ins || '          ,SUM(DECODE( T.GL_KAMOKU_CODE , ''7020'' , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_KSZENJZAN,0) , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_KSZENJZAN,0) * -1 ';
  var_ins := var_ins || '          )) AS GL_KIKAN_KSZENJZAN';
  var_ins := var_ins || '          ,SUM(DECODE( T.GL_KAMOKU_CODE , ''7020'' , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_SRBZAN,0) , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_SRBZAN,0) * -1 ';
  var_ins := var_ins || '          )) AS GL_KIKAN_SRBZAN';
  var_ins := var_ins || ' 		FROM T_ZANCHKGLOVIAZAN T';
  var_ins := var_ins || '           ,M_KKUNITMST M';
  var_ins := var_ins || ' 		WHERE T.GL_KAMOKU_CODE || SUBSTR(T.GL_SAIMOKU_CODE,1,3) IN (''7020110'', ''8061270'', ''8071230'')';
  var_ins := var_ins || ' 		AND T.SYSTM_UNYO_KIGYO_CODE IN (' || shmLst || ')';
  var_ins := var_ins || ' 		AND SUBSTR(T.GL_SAKUHYO_SYURYO_HZK, 1, 6) =' || hzk || '';
  var_ins := var_ins || '       AND T.UNIT_CODE = M.UNIT_CODE(+)';
  var_ins := var_ins || '       AND ''' || tojitsu || ''' BETWEEN M.KKMST_YUKO_KIGEN_KSHB(+) AND M.KKMST_YUKO_KIGEN_SHRYB(+)';
  var_ins := var_ins || '       AND M.SHIYO_KNSH_KUBUN(+) = ''0'' ';
  var_ins := var_ins || ' 		GROUP BY T.SYSTM_UNYO_KIGYO_CODE, SUBSTR(T.UNIT_CODE, 1, 3), T.GL_KAMOKU_CODE || SUBSTR(T.GL_SAIMOKU_CODE,1,3)';
  var_ins := var_ins || '      UNION ALL ';
  var_ins := var_ins || ' 		SELECT T.SYSTM_UNYO_KIGYO_CODE';
  var_ins := var_ins || ' 			,NVL(MAX(M.GROUP_CODE), ''XXXX'') AS GROUP_CODE';
  var_ins := var_ins || '          ,''7200110'' AS KANJO_KMK_CODE';
  var_ins := var_ins || '          ,SUM(DECODE( T.GL_KAMOKU_CODE , ''7200'' , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_KSZENJZAN,0) , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_KSZENJZAN,0) * -1 ';
  var_ins := var_ins || '          )) AS GL_KIKAN_KSZENJZAN';
  var_ins := var_ins || '          ,SUM(DECODE( T.GL_KAMOKU_CODE , ''7200'' , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_SRBZAN,0) , ';
  var_ins := var_ins || ' 			NVL(T.GL_KIKAN_SRBZAN,0) * -1 ';
  var_ins := var_ins || '          )) AS GL_KIKAN_SRBZAN';
  var_ins := var_ins || ' 		FROM T_ZANCHKGLOVIAZAN T';
  var_ins := var_ins || '           ,M_KKUNITMST M';
  var_ins := var_ins || ' 		WHERE T.GL_KAMOKU_CODE || SUBSTR(T.GL_SAIMOKU_CODE,1,3) IN (''7200110'', ''8061270'', ''8071230'')';
  var_ins := var_ins || ' 		AND T.SYSTM_UNYO_KIGYO_CODE IN (' || shmLst || ')';
  var_ins := var_ins || ' 		AND SUBSTR(T.GL_SAKUHYO_SYURYO_HZK, 1, 6) =' || hzk || '';
  var_ins := var_ins || '       AND T.UNIT_CODE = M.UNIT_CODE(+)';
  var_ins := var_ins || '       AND ''' || tojitsu || ''' BETWEEN M.KKMST_YUKO_KIGEN_KSHB(+) AND M.KKMST_YUKO_KIGEN_SHRYB(+)';
  var_ins := var_ins || '       AND M.SHIYO_KNSH_KUBUN(+) = ''0'' ';
  var_ins := var_ins || ' 		GROUP BY T.SYSTM_UNYO_KIGYO_CODE, SUBSTR(T.UNIT_CODE, 1, 3), T.GL_KAMOKU_CODE || SUBSTR(T.GL_SAIMOKU_CODE,1,3)';
  var_ins := var_ins || ' 		) W';
  var_ins := var_ins || ' 	GROUP BY W.SYSTM_UNYO_KIGYO_CODE, W.GROUP_CODE,W.KANJO_KMK_CODE';
  var_ins := var_ins || ' 	) A,';
  var_ins := var_ins || ' 	(SELECT SYSTM_UNYO_KIGYO_CODE,GROUP_CODE,SUM(NVL(KINGAKU,0)) AS KINGAKU ,KANJO_KMK_CODE';
  var_ins := var_ins || ' 	FROM (SELECT SYSTM_UNYO_KIGYO_CODE,GROUP_CODE, NVL(KANRI_KK_KNGK_TSUKI,0) AS KINGAKU ,''7020110'' AS KANJO_KMK_CODE';
  var_ins := var_ins || ' 		FROM ' || tmp_keihiyosan || '  ';
  var_ins := var_ins || '		WHERE KEIRI_JM_NGSTU = ' || hzk || '';
  var_ins := var_ins || ' 	     AND KANRI_KK_KNJKM IN (' || dk_list || ') UNION ALL';
  var_ins := var_ins || ' 	SELECT SYSTM_UNYO_KIGYO_CODE,GROUP_CODE, NVL(KANRI_KK_KNGK_TSUKI,0) AS KINGAKU ,''7200110'' AS KANJO_KMK_CODE';
  var_ins := var_ins || ' 		FROM ' || tmp_keihiyosan || '  ';
  var_ins := var_ins || ' 		WHERE KEIRI_JM_NGSTU = ' || hzk || '';
  var_ins := var_ins || ' 	     AND KANRI_KK_KNJKM IN (' || srk_list || ') )';
  var_ins := var_ins || '	GROUP BY SYSTM_UNYO_KIGYO_CODE,GROUP_CODE,KANJO_KMK_CODE) B,';
  var_ins := var_ins || ' 	M_KKGROUPMST C';
  var_ins := var_ins || ' WHERE A.SYSTM_UNYO_KIGYO_CODE = B.SYSTM_UNYO_KIGYO_CODE(+)';
  var_ins := var_ins || ' 	AND A.GROUP_CODE = B.GROUP_CODE(+)';
  var_ins := var_ins || ' 	AND A.KANJO_KMK_CODE = B.KANJO_KMK_CODE(+)';
  var_ins := var_ins || ' 	AND A.GROUP_CODE = C.GROUP_CODE(+)';
  var_ins := var_ins || ' 	AND ''' || tojitsu || ''' BETWEEN C.KKMST_YUKO_KIGEN_KSHB(+) AND C.KKMST_YUKO_KIGEN_SHRYB(+)';
  var_ins := var_ins || '   AND C.SHIYO_KNSH_KUBUN(+) = ''0'' ';

  RETURN var_ins;
 END;



--**********************************************************************************
-- FUNCTION INSERT_GLOVIAZAN
-- 結合元不足レコード一括登録
--**********************************************************************************
  FUNCTION INSERT_GLOVIAZAN(kanjocode VARCHAR2) RETURN NUMBER IS
  BEGIN

	INSERT INTO T_ZANCHKGLOVIAZAN
	 SELECT
	 A.SYSTM_UNYO_KIGYO_CODE
	,A.UNIT_CODE
	,NULL
	,SUBSTR(kanjocode,1,4)
	,SUBSTR(kanjocode,5,7)
	,tojitsu
	,0
	,0
	,'PLKL010'
	,A.SYSTM_UNYO_KIGYO_CODE
	,NULL
	,'PLKL010'
	,A.SYSTM_UNYO_KIGYO_CODE
	,NULL
	,1
	,TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')
	,TO_CHAR(sysdate, 'YYYYMMDDHH24MISS')
	FROM T_KEIHIYOSAN A
	WHERE NOT EXISTS(SELECT 'X' FROM T_ZANCHKGLOVIAZAN C
	WHERE A.UNIT_CODE = C.UNIT_CODE
	AND C.GL_KAMOKU_CODE || C.GL_SAIMOKU_CODE = kanjocode )
	GROUP BY A.SYSTM_UNYO_KIGYO_CODE
	,A.UNIT_CODE ;

	 RETURN 0;

  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('結合元不足レコード一括登録処理にてエラーが発生しました。');
      DBMS_OUTPUT.PUT_LINE('SQL コード = ' || sqlcode);
      DBMS_OUTPUT.PUT_LINE('エラー内容 = ' || sqlerrm(sqlcode));
      RETURN 10;
 END;



--**********************************************************************************
-- FUNCTION SELECT_KANJO_LIST
--管理会計科目集約マスタからパラメータ「syku_kjcode」で指定した親勘定科目コードで
--子勘定科目コードを取得する。
--**********************************************************************************
   FUNCTION SELECT_KANJO_LIST(syku_kjcode VARCHAR2, param NUMBER) RETURN VARCHAR2 IS
    BEGIN
    kanjo_list := '';
     FOR rec IN (SELECT KANRI_KK_KNJKM  FROM (SELECT DISTINCT * FROM (
        SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_1 as KANRI_KK_SHK_KMK_CODE
           , KANRI_KK_SHK_UCHWK_CODE_1 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_2 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_2 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_3 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_3 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_4 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_4 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_5 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_5 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_6 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_6 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_7 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_7 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_8 as KANRI_KK_SHK_KMK_CODE
         , KANRI_KK_SHK_UCHWK_CODE_8 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
         FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI , KANRI_KK_SHK_KMK_CODE_9 as KANRI_KK_SHK_KMK_CODE
          , KANRI_KK_SHK_UCHWK_CODE_9 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE    FROM M_KKKMKSHUYAKU K
         UNION ALL
         SELECT   KANRI_KK_KNJKM , KANRI_KK_UCHWK_CODE , KANRI_KK_KNJKM_MEI , KANRI_KK_SMK_MEI
         , KANRI_KK_SHK_KMK_CODE_10 as KANRI_KK_SHK_KMK_CODE   , KANRI_KK_SHK_UCHWK_CODE_10 as KANRI_KK_SHK_UCHWK_CODE , SYSTM_UNYO_KIGYO_CODE
     FROM M_KKKMKSHUYAKU K))
    WHERE      SYSTM_UNYO_KIGYO_CODE IN (SELECT SYSTM_UNYO_KIGYO_CODE FROM T_ZANCHK_NENGETSUJICONTROL WHERE KEIRI_SHMB >= tojitsu OR param = 1)
       AND KANRI_KK_SHK_KMK_CODE <> '       ' AND LEVEL < 10
     START WITH    (KANRI_KK_SHK_KMK_CODE,KANRI_KK_SHK_UCHWK_CODE) IN    ((syku_kjcode , '            '))
     CONNECT BY PRIOR KANRI_KK_KNJKM=KANRI_KK_SHK_KMK_CODE
     AND KANRI_KK_UCHWK_CODE = KANRI_KK_SHK_UCHWK_CODE)

     LOOP
        kanjo_list := kanjo_list || '''' || rec.KANRI_KK_KNJKM || ''',';
     END LOOP;

    IF (LENGTH(kanjo_list) > 0) THEN
      kanjo_list := SUBSTR(kanjo_list, 1, LENGTH(kanjo_list)-1);
    END IF;

   RETURN kanjo_list;
   END;




--**********************************************************************************
--
--メイン処理開始
--
--**********************************************************************************

 BEGIN
  -- 当日、当月、前月を取得
  var_select := 'SELECT GROUP_EGYB, SUBSTR(GROUP_EGYB, 1, 6), TO_CHAR(ADD_MONTHS(TO_DATE(GROUP_EGYB, ''YYYYMMDD''), -1), ''YYYYMM'')';
  var_select := var_select || 'FROM M_YAKANHIZUKEKANRIMST WHERE GROUP_CODE =''0000''';

  BEGIN
    EXECUTE IMMEDIATE(var_select) INTO tojitsu, togetsu, zengetsu;
  EXCEPTION
    -- 取得できなかった場合、処理終了
    WHEN NO_DATA_FOUND THEN
      DBMS_OUTPUT.PUT_LINE('夜間日付管理マスタに該当するデータが存在しませんでした。' );
      out_para := 10;
      GOTO ERR_RESULT;
  END;

    --ＧＬＯＶＩＡ残高テーブル対象無データ一括登録
	RET  :=INSERT_GLOVIAZAN('7020110');  --売上高
	RET  :=INSERT_GLOVIAZAN('7200110');  --売上総利益

  -- 締め前システム運用企業コードリスト取得(年月次コントロールマスタ)
  FOR rec IN (SELECT SYSTM_UNYO_KIGYO_CODE FROM T_ZANCHK_NENGETSUJICONTROL WHERE KEIRI_SHMB >= tojitsu ORDER BY SYSTM_UNYO_KIGYO_CODE) LOOP
    shm_before := shm_before || rec.SYSTM_UNYO_KIGYO_CODE || ',';
  END LOOP;
  IF (LENGTH(shm_before) > 0) THEN
    shm_before := SUBSTR(shm_before, 1, LENGTH(shm_before)-1);
  END IF;


  -- 締め後システム運用企業コードリスト取得
  FOR rec IN (SELECT SYSTM_UNYO_KIGYO_CODE FROM T_ZANCHK_NENGETSUJICONTROL ORDER BY SYSTM_UNYO_KIGYO_CODE) LOOP
    shm_after := shm_after || rec.SYSTM_UNYO_KIGYO_CODE || ',';
  END LOOP;
  IF (LENGTH(shm_after) > 0) THEN
    shm_after := SUBSTR(shm_after, 1, LENGTH(shm_after)-1);
  END IF;
  BEGIN
  --一時テーブルを作成（締前）
  IF (LENGTH(shm_before) > 0) THEN
    creattab := '';
    creattab := creattab || '   CREATE GLOBAL TEMPORARY TABLE TMP_KEIHIYOSAN_BEFROE ON COMMIT PRESERVE ROWS ';
    --締前ー経費予算データ取得
    kanjo_list_before := creattab || 'AS SELECT SYSTM_UNYO_KIGYO_CODE,KSSNZ,KANRI_KK_NENDO,KEIRI_JM_NGSTU,KANRI_KK_KNGK_TSUKI,GROUP_CODE,KANRI_KK_KNJKM FROM (';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 1) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 2) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 3) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 4) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 5) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 6) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 7) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 8) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 9) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 10) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 11) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || UNION_ALL(shm_before, 12) || ' UNION ALL ';
    kanjo_list_before := kanjo_list_before || 'SELECT ';
    kanjo_list_before := kanjo_list_before || '    T.SYSTM_UNYO_KIGYO_CODE ';
    kanjo_list_before := kanjo_list_before || '  , ''000000'' AS KSSNZ ';
    kanjo_list_before := kanjo_list_before || '  , 0 AS KANRI_KK_NENDO ';
    kanjo_list_before := kanjo_list_before || '  , SUBSTR(T.KEIJO_BI,1,6) AS KEIRI_JM_NGSTU ';
    kanjo_list_before := kanjo_list_before || '  , CASE ';
    kanjo_list_before := kanjo_list_before || '        WHEN M.NAIYO_MJRTS_5 = ''1'' THEN NVL(SUM(T.KNGK), 0) ';
    kanjo_list_before := kanjo_list_before || '        ELSE NVL(SUM(T.BB_SAEKI), 0) ';
    kanjo_list_before := kanjo_list_before || '    END AS KANRI_KK_KNGK_TSUKI ';
    kanjo_list_before := kanjo_list_before || '  , T.KJGRP AS  GROUP_CODE ';
    kanjo_list_before := kanjo_list_before || '  , M.KEY_KMK_3 AS KANRI_KK_KNJKM ';
    kanjo_list_before := kanjo_list_before || 'FROM ';
    kanjo_list_before := kanjo_list_before || '    T_KANSETSURIEKI T ';
    kanjo_list_before := kanjo_list_before || '  , M_CODEMASTER M ';
    kanjo_list_before := kanjo_list_before || 'WHERE SUBSTR(T.KEIJO_BI,1,6) = ''' || zengetsu || '''  ';
    kanjo_list_before := kanjo_list_before || '    AND M.KEY_KMK_1               = T.DMPY_KUBUN ';
    kanjo_list_before := kanjo_list_before || '    AND M.KEY_KMK_2               = T.ZAIKO_CHKS_KUBUN ';
    kanjo_list_before := kanjo_list_before || '    AND M.SUB_SYSTM_ID            = ''KG'' ';
    kanjo_list_before := kanjo_list_before || '    AND M.CODE_BNR                = ''173'' ';
    kanjo_list_before := kanjo_list_before || '    AND M.NAIYO_SUCHI_3 IN (''1'', ''2'') ';
    kanjo_list_before := kanjo_list_before || '    AND M.KKMST_YUKO_KIGEN_KSHB  <= ''' || tojitsu || ''' ';
    kanjo_list_before := kanjo_list_before || '    AND M.KKMST_YUKO_KIGEN_SHRYB >= ''' || tojitsu || ''' ';
    kanjo_list_before := kanjo_list_before || '    AND M.SHIYO_KNSH_KUBUN        = ''0'' ';
    kanjo_list_before := kanjo_list_before || 'GROUP BY ';
    kanjo_list_before := kanjo_list_before || '    T.SYSTM_UNYO_KIGYO_CODE ';
    kanjo_list_before := kanjo_list_before || '  , T.KJGRP ';
    kanjo_list_before := kanjo_list_before || '  , M.KEY_KMK_3 ';
    kanjo_list_before := kanjo_list_before || '  , M.NAIYO_MJRTS_5 ';
    kanjo_list_before := kanjo_list_before || '  , SUBSTR(T.KEIJO_BI,1,6) ';
    kanjo_list_before := kanjo_list_before || ') WHERE KEIRI_JM_NGSTU = ''' || zengetsu || '''';
    --DBMS_OUTPUT.PUT_LINE('締前勘定科目リスト作成=' || SUBSTR(kanjo_list_before,1,20000) );
    --DBMS_OUTPUT.PUT_LINE('締前勘定科目リスト作成=' || SUBSTR(kanjo_list_before,20001) );
    EXECUTE IMMEDIATE(kanjo_list_before);
  END IF;

  --一時テーブルを作成（締後）
  IF (LENGTH(shm_after) > 0) THEN
    creattab := '';
    creattab := creattab || '   CREATE GLOBAL TEMPORARY TABLE TMP_KEIHIYOSAN_AFTER ON COMMIT PRESERVE ROWS ';
    --締後ー経費予算データ取得
    kanjo_list_after := creattab || 'AS SELECT SYSTM_UNYO_KIGYO_CODE,KSSNZ,KANRI_KK_NENDO,KEIRI_JM_NGSTU,KANRI_KK_KNGK_TSUKI,GROUP_CODE,KANRI_KK_KNJKM FROM (';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 1) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 2) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 3) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 4) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 5) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 6) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 7) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 8) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 9) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 10) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 11) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || UNION_ALL(shm_after, 12) || ' UNION ALL ';
    kanjo_list_after := kanjo_list_after || 'SELECT ';
    kanjo_list_after := kanjo_list_after || '    T.SYSTM_UNYO_KIGYO_CODE ';
    kanjo_list_after := kanjo_list_after || '  , ''000000'' AS KSSNZ ';
    kanjo_list_after := kanjo_list_after || '  , 0 AS KANRI_KK_NENDO ';
    kanjo_list_after := kanjo_list_after || '  , SUBSTR(T.KEIJO_BI,1,6) AS KEIRI_JM_NGSTU ';
    kanjo_list_after := kanjo_list_after || '  , CASE ';
    kanjo_list_after := kanjo_list_after || '        WHEN M.NAIYO_MJRTS_5 = ''1'' THEN NVL(SUM(T.KNGK), 0) ';
    kanjo_list_after := kanjo_list_after || '        ELSE NVL(SUM(T.BB_SAEKI), 0) ';
    kanjo_list_after := kanjo_list_after || '    END AS KANRI_KK_KNGK_TSUKI ';
    kanjo_list_after := kanjo_list_after || '  , T.KJGRP AS  GROUP_CODE ';
    kanjo_list_after := kanjo_list_after || '  , M.KEY_KMK_3 AS KANRI_KK_KNJKM ';
    kanjo_list_after := kanjo_list_after || 'FROM ';
    kanjo_list_after := kanjo_list_after || '    T_KANSETSURIEKI T ';
    kanjo_list_after := kanjo_list_after || '  , M_CODEMASTER M ';
    kanjo_list_after := kanjo_list_after || 'WHERE SUBSTR(T.KEIJO_BI,1,6) = ''' || togetsu || '''  ';
    kanjo_list_after := kanjo_list_after || '    AND M.KEY_KMK_1               = T.DMPY_KUBUN ';
    kanjo_list_after := kanjo_list_after || '    AND M.KEY_KMK_2               = T.ZAIKO_CHKS_KUBUN ';
    kanjo_list_after := kanjo_list_after || '    AND M.SUB_SYSTM_ID            = ''KG'' ';
    kanjo_list_after := kanjo_list_after || '    AND M.CODE_BNR                = ''173'' ';
    kanjo_list_after := kanjo_list_after || '    AND M.NAIYO_SUCHI_3 IN (''1'', ''2'') ';
    kanjo_list_after := kanjo_list_after || '    AND M.KKMST_YUKO_KIGEN_KSHB  <= ''' || tojitsu || ''' ';
    kanjo_list_after := kanjo_list_after || '    AND M.KKMST_YUKO_KIGEN_SHRYB >= ''' || tojitsu || ''' ';
    kanjo_list_after := kanjo_list_after || '    AND M.SHIYO_KNSH_KUBUN        = ''0'' ';
    kanjo_list_after := kanjo_list_after || 'GROUP BY ';
    kanjo_list_after := kanjo_list_after || '    T.SYSTM_UNYO_KIGYO_CODE ';
    kanjo_list_after := kanjo_list_after || '  , T.KJGRP ';
    kanjo_list_after := kanjo_list_after || '  , M.KEY_KMK_3 ';
    kanjo_list_after := kanjo_list_after || '  , M.NAIYO_MJRTS_5 ';
    kanjo_list_after := kanjo_list_after || '  , SUBSTR(T.KEIJO_BI,1,6) ';
    kanjo_list_after := kanjo_list_after || ') WHERE KEIRI_JM_NGSTU = ''' || togetsu || '''';
    EXECUTE IMMEDIATE(kanjo_list_after);
  END IF;
    END;

  BEGIN
    -- 残高チェック結果登録（締め中前月分）
    IF (LENGTH(shm_before) > 0) THEN
      urg_dk_list := SELECT_KANJO_LIST('7020110',0);
      urg_srk_list := SELECT_KANJO_LIST('7200110',0);
      IF (LENGTH(urg_dk_list) > 0 AND LENGTH(urg_srk_list) > 0) THEN
        var_sql := '';
        var_sql := SQL_INSERT(shm_before, zengetsu, '2', urg_dk_list, urg_srk_list,'TMP_KEIHIYOSAN_BEFROE');--締め中前月分の場合、2(前月)
        DBMS_OUTPUT.PUT_LINE('締前チェック結果登録SQL文=' || var_sql );
        EXECUTE IMMEDIATE(var_sql);
      END IF;

    END IF;

    -- 残高チェック結果登録（締め後）
    IF (LENGTH(shm_after) > 0) THEN
      urg_dk_list := SELECT_KANJO_LIST('7020110',1);
      urg_srk_list := SELECT_KANJO_LIST('7200110',1);
      IF (LENGTH(urg_dk_list) > 0 AND LENGTH(urg_srk_list) > 0) THEN
        var_sql := '';
        var_sql := SQL_INSERT(shm_after, togetsu, '1', urg_dk_list, urg_srk_list,'TMP_KEIHIYOSAN_AFTER');--締め後当月分の場合、1(当月)
        DBMS_OUTPUT.PUT_LINE('締後チェック結果登録SQL文=' || var_sql );
        EXECUTE IMMEDIATE(var_sql);
      END IF;
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('残高チェック結果登録処理にてエラーが発生しました。');
      DBMS_OUTPUT.PUT_LINE('SQL コード = ' || sqlcode);
      DBMS_OUTPUT.PUT_LINE('エラー内容 = ' || sqlerrm(sqlcode));
      out_para := 10;
      GOTO ERR_RESULT;
  END;
  COMMIT;

  -- 残高チェック結果確認
  var_sql := '';
  var_sql := 'SELECT COUNT(*) AS GOSA_CNT FROM T_ZANCHECKKEKKA A WHERE A.ZANCHECK_NO = ''11'' AND A.TJTS_HZK <= ''' || tojitsu || ''' AND A.GOSA_KINGAKU <> 0 ';
  var_sql := var_sql || 'AND NOT EXISTS(SELECT ''X'' FROM M_CODEMASTER ';--コードマスタ
  var_sql := var_sql || 'WHERE SUB_SYSTM_ID = ''KL'' ';
  var_sql := var_sql || 'AND CODE_BNR = ''PLKL010'' ';
  var_sql := var_sql || 'AND KEY_KMK_1 = ''GROUP_CODE'' ';
  var_sql := var_sql || 'AND KEY_KMK_2 = A.GROUP_CODE) ';

  BEGIN
    EXECUTE IMMEDIATE(var_sql) INTO gosa_cnt;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN NULL;
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('ＰＬ累計額チェック処理にてエラーが発生しました。');
      DBMS_OUTPUT.PUT_LINE('SQL コード = ' || sqlcode);
      DBMS_OUTPUT.PUT_LINE('エラー内容 = ' || sqlerrm(sqlcode));
      out_para := 10;
      GOTO ERR_RESULT;
  END;

  -- 誤差ありの場合
  IF (gosa_cnt > 0) THEN
    DBMS_OUTPUT.PUT_LINE('チェック対象の残高金額に誤差発生しています。');
    out_para := 10;
    GOTO ERR_RESULT;
  END IF;

  -- 正常終了
  out_para := 0;

<<ERR_RESULT>>
  NULL;
   --一時テーブルを削除
   IF (LENGTH(shm_before) > 0) THEN
     EXECUTE IMMEDIATE('TRUNCATE TABLE TMP_KEIHIYOSAN_BEFROE');
     EXECUTE IMMEDIATE('DROP TABLE TMP_KEIHIYOSAN_BEFROE');
   END IF;
   IF (LENGTH(shm_after) > 0) THEN
     EXECUTE IMMEDIATE('TRUNCATE TABLE TMP_KEIHIYOSAN_AFTER');
     EXECUTE IMMEDIATE('DROP TABLE TMP_KEIHIYOSAN_AFTER');
   END IF;

END PLKL010;