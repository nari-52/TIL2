    show user;
    -- USER이(가) "MYORAUSER"입니다.
    
    select * from tab;
    
    select *
    from user_sequences;
    
    drop table jdbc_tbl_memo purge;
    
    drop sequence JDBC_SEQ_MEMO ;
    
    insert into jdbc_tbl_memo (no, name, msg) 
    values(2, '하하호호', '안녕');
    -- 1 행 이(가) 삽입되었습니다.
    
    update jdbc_tbl_memo set name = '안중근';
    -- 2개 행 이(가) 업데이트되었습니다.
    
    delete from  jdbc_tbl_memo;
    -- 2개 행 이(가) 삭제되었습니다.
    
    rollback;
    
    select *
    from jdbc_tbl_memo;
    
    String sql = "select no, name, msg\n"+
"            , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday\n"+
"    from jdbc_tbl_memo\n"+
"    order by no desc";
    
select no, name, msg
        , to_char(writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday
from jdbc_tbl_memo
where msg like '%' || '안녕' || '%';

    
update jdbc_tbl_memo set msg = '바꿔봅시다';
where no = '1';

update jdbc_tbl_memo set msg = '0'
where no = '0';

rollback;

commit;

    select *
    from jdbc_tbl_memo;

String sql = "delete from jdbc_tbl_memo \n"+
"where no = 1;";

    commit;
    
    select *
    from jdbc_tbl_memo2;
    
    select *
    from jdbc_tbl_memo2
    where no = 2;
    
    select *
    from jdbc_tbl_memo2
    where no = '2';
    
    select *
    from jdbc_tbl_memo2
    where name = '김건형';
    
    select *
    from jdbc_tbl_memo2
    where to_char(no) = '하하하';
    
    
    select *
    from jdbc_tbl_memo2
    where to_char(no) = '2';
    
    select *
    from user_tables
    where table_name = 'JDBC_TBL_MEMO2' ;
    
    select *
    from user_sequences
    where sequence_name = 'JDBC_SEQ_MEMO2' ;
    
    
String sql = "select *\n"+
"from user_tables\n"+
"where table_name = 'JDBC_TBL_MEMO2' ;";
    
String sql = "drop table jdbc_tbl_memo2 purge; ";   

String sql = "drop sequence JDBC_SEQ_MEMO2 ;";






    --- /// 회원 테이블 생성하기 (부모) /// ---
    
    drop  table jdbc_member purge;
    
    
    create table jdbc_member
    ( userseq               number not null             -- 회원번호
    , userid                  varchar2(30) not null         -- 회원 아이디
    , passwd                 varchar2(30) not null         -- 회원 암호
    , name                  varchar2(20) not null         -- 회원명
    , mobile                  varchar2(20)                      -- 연락처
    , point                  number(10) default 0             -- 포인트
    , registerday           date default sysdate                -- 가입일자
    , status                   number(1) default 1 -- status 컬럼의 값이 1이면 정상, 0 이면 탈퇴
    , constraint PK_jdbc_member Primary Key (userseq)
    , constraint UK_jdbc_member Unique (userid)
    , constraint CK_jdbc_member check (status in (0,1) )
    );
    -- Table JDBC_MEMBER이(가) 생성되었습니다.
    
    alter table jdbc_member
    add constraint CK_jdbc_member_point check (point < 30);
    
    create sequence user_seq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    -- Sequence USER_SEQ이(가) 생성되었습니다.


    

    --- /// 게시판 테이블 생성하기 /// ---
    
    
    create table jdbc_board
    (boardno           number             not null                -- 글번호
    ,fk_userid           varchar2(30)     not null                 -- 작성자아이디
    ,subject              varchar2(100)    not null                -- 글제목
    ,contents          varchar2(200)   not null                -- 글내용
    ,writeday            date default sysdate not null      -- 작성일자
    ,viewconunt        number default 0 not null           -- 조회수
    ,boardpasswd    varchar2(20) not null                     -- 글암호
    ,constraint PK_jdbc_board primary key(boardno)
    ,constraint FK_jdbc_board foreign key(fk_userid) references jdbc_member(userid) 
    );
    
    
    drop table jdbc_board purge;
    
--    create table jdbc_board
--    ( boardno                   number not null                        -- 글번호
--    , fk_userid                   varchar2(30) not null                 -- 작성자아이디
--    , subject                     varchar2(100) not null              -- 글제목
--    , contents                  varchar2(200) not null              -- 글내용
--    , writeday                  date default sysdate not null       -- 글 작성일자
--    , viewcount                 number default 0 not null          -- 조회수
--    , boardpasswd              varchar2(20) not null                -- 글암호
--    , constraint PK_ jdbc_board primary key (boardno)
--    , constraint FK_ jdbc_board Foreign Key (fk_userid) references jdbc_member(userid) 
--    );
    
    create sequence board_seq
    start with 1
    increment by 1
    nomaxvalue
    nominvalue
    nocycle
    nocache;
    
    
    select *
    from jdbc_member
    order by userseq desc;
    
    select userseq, userid, passwd, name, mobile, point, to_char(registerday, 'yyyy-mm-dd') AS registerday
    from jdbc_member
    where status = 1
    and userid = 'leess'
    and passwd = 'qwer1234$';
    
    select *
    from jdbc_board;
    
    delete from jdbc_board
    where fk_userid = 'eomjh'; -- 작성 글 지우기
    
    rollback;
    
    commit;
    
    --- 글번호     글제목     글쓴이     작성일자    조회수
        -------      -------     #####     --------     ------
    jdbc_board              jdbc_member
    
    
    desc jdbc_board;       -- 자식테이블     -- 조인조건절 컬럼 : FK_USERID   NOT NULL VARCHAR2(30)  
    desc jdbc_member;    -- 부모테이블     -- 조인조건절 컬럼 : USERID      NOT NULL VARCHAR2(30) 
    -- 둘 다 not null이기 때문에 left JOIN이나 right JOIN을 할 필요가 없다.
    
    select B.boardno
            , B.subject
            , M.name 
            , to_char (B.writeday, 'yyyy-mm-dd hh24:mi:ss') AS writeday
            , B.viewconunt 
    from jdbc_board B JOIN jdbc_member M
    on B.fk_userid = M.userid
    order by 1 desc


    select contents
            , fk_userid
    from jdbc_board
    where boardno = 6;

update tbl_cafe_member set point = point + 10
where boardNo = 'boardNo';

update jdbc_board set viewconunt = viewconunt + 1 
where boardNo = 6;

commit;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    