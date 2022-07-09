# Interpreter-for-Core-language
There is a newly designed language called Core. This repo contains the scanner, parser, executor, semantic checker of the Core language.

Please note this is a language like C or Java where whitespaces have no meaning, and whitespace can be inserted between keywords, identifiers, constants, and specials to accommodate programmer style. This grammar does not include formal rules about whitespace because that would add immense clutter.

Here is the Core language:

<prog> ::= program <decl-seq> begin <stmt-seq> end | program begin <stmt-seq> end
<decl-seq> ::= <decl> | <decl><decl-seq> | <func-decl> | <func-decl><decl-seq>
<stmt-seq> ::= <stmt> | <stmt><stmt-seq>
<decl> ::= <decl-int> | <decl-ref>
<decl-int> ::= int <id-list> ;
<decl-ref> ::= ref <id-list> ;
<id-list> ::= id | id , <id-list>
<func-decl> ::= id ( ref <formals> ) { <stmt-seq> }
<formals> ::= id | id , <formals>
<stmt> ::= <assign> | <if> | <loop> | <out> | <decl> | <func-call>
<func-call> ::= begin id ( <formals> ) ;
<assign> ::= id = input ( ) ; | id = <expr> ; | id = new class; | id = share id ; <out> ::= output ( <expr> ) ;
<if> ::= if <cond> then { <stmt-seq> } | if <cond> then { <stmt-seq> } else { <stmt-seq> }
<loop> ::= while <cond> { <stmt-seq> }
<cond> ::= <cmpr> | ! ( <cond> ) | <cmpr> or <cond>
<cmpr> ::= <expr> == <expr> | <expr> < <expr> | <expr> <= <expr>
<expr> ::= <term> | <term> + <expr> | <term> – <expr>
<term> ::= <factor> | <factor> * <term>
<factor> ::= id | const | ( <expr> )

*Some known bugs: to make it easier to create this language, Core language CANNOT handle expressions like "1 - 2 - 3". Normally, 1 - 2 - 3 = -4, but Core language will calculate it as 1 - (2 - 3) = 2. This is because in the executor, the program will start calculating the parse tree from the right-buttom nodes.
