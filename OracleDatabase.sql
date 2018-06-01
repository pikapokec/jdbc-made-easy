CREATE TABLE TEST_4_SELECT
(
  SOME_STRING   VARCHAR2(100 BYTE),
  JUST_DATE     DATE,
  SOME_INTEGER  NUMBER(4),
  CURRENCY      NUMBER,
  STATUS        VARCHAR2(1 BYTE),
  TS            TIMESTAMP(6)
);

Insert into TEST_4_SELECT
   (SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS,
    TS)
 Values
   ('A', TO_DATE('02/16/2018 11:24:04', 'MM/DD/YYYY HH24:MI:SS'), 123, 12345.67, '0',
    TO_TIMESTAMP('01.02.2003 04:05:06.789000','DD.MM.YYYY HH24:MI:SS.FF'));
Insert into TEST_4_SELECT
   (SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS,
    TS)
 Values
   ('B', TO_DATE('01/01/2000 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), -123, -12345.67, '1',
    NULL);
Insert into TEST_4_SELECT
   (SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS,
    TS)
 Values
   ('C', TO_DATE('02/01/2003 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 0, 0, '2',
    NULL);
Insert into TEST_4_SELECT
   (SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS,
    TS)
 Values
   ('D', NULL, NULL, NULL, '?',
    NULL);
COMMIT;

--

CREATE OR REPLACE PACKAGE pckTestEasyJDBC AS

  Type TTestForSelect Is Table Of TEST_4_SELECT%ROWTYPE;

  Function F_RefCursor(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date) RETURN SYS_REFCURSOR;

  Procedure P_RefCursor(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date,
          outCursor     out SYS_REFCURSOR);

  Function F_Function(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date) RETURN VarChar2;

  Procedure P_Procedure(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date,
          result        out VarChar2);

  Procedure P_Lob_Procedure(
          inId          in Integer,
          outBLOB       out BLOB,
          outCLOB       out CLOB);

  Function GetTestForSelect(inString in VarChar2,
                            inDate in Date,
                            inInteger in Number) Return TTestForSelect Pipelined;


END pckTestEasyJDBC;
/

CREATE OR REPLACE PACKAGE BODY pckTestEasyJDBC AS

  Function F_RefCursor(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date) RETURN SYS_REFCURSOR Is


    sCursor SYS_REFCURSOR;

  Begin

    inoutNumber := inNumber + 1;
    inoutText := inText || 'inout';
    inoutDate := inDate + 1;
    outNumber := inNumber + 2;
    outText := inText || 'out';
    outDate := inDate + 2;

    Open sCursor For
        SELECT SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS, TS
        FROM   TEST_4_SELECT
        Order By 1;

    Return sCursor;

  End F_RefCursor;

  Procedure P_RefCursor(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date,
          outCursor     out SYS_REFCURSOR) Is

  Begin

    inoutNumber := inNumber + 1;
    inoutText := inText || 'inout';
    inoutDate := inDate + 1;
    outNumber := inNumber + 2;
    outText := inText || 'out';
    outDate := inDate + 2;

    Open outCursor For
        SELECT SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS, TS
        FROM   TEST_4_SELECT
        Order By 1;

  End P_RefCursor;

  Function F_Function(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date) RETURN VarChar2 Is
  Begin

    inoutNumber := inNumber + 1;
    inoutText := inText || 'inout';
    inoutDate := inDate + 1;
    outNumber := inNumber + 2;
    outText := inText || 'out';
    outDate := inDate + 2;

    Return 'Test if function F is working!';

  End F_Function;

  Procedure P_Procedure(
          inNumber      in Number,
          inText        in VarChar2,
          inDate        in Date,
          inoutNumber   in out Number,
          inoutText     in out VarChar2,
          inoutDate     in out Date,
          outNumber     out Number,
          outText       out VarChar2,
          outDate       out Date,
          result        out VarChar2) Is
  Begin

    inoutNumber := inNumber + 1;
    inoutText := inText || 'inout';
    inoutDate := inDate + 1;
    outNumber := inNumber + 2;
    outText := inText || 'out';
    outDate := inDate + 2;

    result := 'Test if procedure P is working!';

  End P_Procedure;

  Procedure P_Lob_Procedure(
          inId          in Integer,
          outBLOB       out BLOB,
          outCLOB       out CLOB) Is
  Begin

    Select RAW_DATA, TEXT_DATA
    Into   outBLOB,  outCLOB
    From   TEST_CLOB_BLOB
    Where  ID = inId;

  End;

  Function GetTestForSelect(inString in VarChar2,
                            inDate in Date,
                            inInteger in Number) Return TTestForSelect Pipelined Is
  Begin

    For rec in (
        SELECT SOME_STRING, JUST_DATE, SOME_INTEGER, CURRENCY, STATUS, TS
        From TEST_4_SELECT
        Where (SOME_STRING=inString or JUST_DATE=inDate or SOME_INTEGER=inInteger)
        Order By 1)
    Loop
        pipe row (rec);
    End Loop;

  End GetTestForSelect;

END pckTestEasyJDBC;
/
