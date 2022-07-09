# Interpreter-for-Core-language
There is a newly designed language called Core. This repo contains the scanner, parser, executor, semantic checker of the Core language.

Please note this is a language like C or Java where whitespaces have no meaning, and whitespace can be inserted between keywords, identifiers, constants, and specials to accommodate programmer style. This grammar does not include formal rules about whitespace because that would add immense clutter.

Here is the Core language:

\<prog\> ::= program \<decl-seq\> begin \<stmt-seq\> end | program begin \<stmt-seq\> end<br />
\<decl-seq\> ::= \<decl\> | \<decl\>\<decl-seq\> | \<func-decl\> | \<func-decl\>\<decl-seq\><br />
\<stmt-seq\> ::= \<stmt\> | \<stmt\>\<stmt-seq\><br />
\<decl\> ::= \<decl-int\> | \<decl-ref\><br />
\<decl-int\> ::= int \<id-list\> ;<br />
\<decl-ref\> ::= ref \<id-list\> ;<br />
\<id-list\> ::= id | id , \<id-list\><br />
\<func-decl\> ::= id ( ref \<formals\> ) { \<stmt-seq\> }<br />
\<formals\> ::= id | id , \<formals\><br />
\<stmt\> ::= \<assign\> | \<if\> | \<loop\> | \<out\> | \<decl\> | \<func-call\><br />
\<func-call\> ::= begin id ( \<formals\> ) ;<br />
\<assign\> ::= id = input ( ) ; | id = \<expr\> ; | id = new class; | id = share id ;<br />
\<out\> ::= output ( \<expr\> ) ;<br />
\<if\> ::= if \<cond\> then { \<stmt-seq\> } | if \<cond\> then { \<stmt-seq\> } else { \<stmt-seq\> }<br />
\<loop\> ::= while \<cond\> { \<stmt-seq\> }<br />
\<cond\> ::= \<cmpr\> | ! ( \<cond\> ) | \<cmpr\> or \<cond\><br />
\<cmpr\> ::= \<expr\> == \<expr\> | \<expr\> < \<expr\> | \<expr\> <= \<expr\><br />
\<expr\> ::= \<term\> | \<term\> + \<expr\> | \<term\> – \<expr\><br />
\<term\> ::= \<factor\> | \<factor\> * \<term\><br />
\<factor\> ::= id | const | ( \<expr\> )<br />

Some known bugs:
To make it easier to create this language, Core language CANNOT handle expressions like "1 - 2 - 3". Normally, 1 - 2 - 3 = -4, but Core language will calculate it as 1 - (2 - 3) = 2. This is because in the executor, the program will start calculating the parse tree from the right-buttom nodes.
