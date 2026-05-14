grammar Program;

@header {
    package com.inm.antlr4;
}

start : ID EOF ;

ID : [a-zA-Z][a-zA-Z0-9]*
     {
       String txt = getText();
       if (txt.length() > 16) {
           txt = txt.substring(0, 16);
           setText(txt);
       }
     } ;

WS : [ \t\r\n]+ -> skip ;