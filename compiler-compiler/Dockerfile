FROM alpine:3.22.4

RUN apk add --no-cache bison flex gcc musl-dev

WORKDIR /cc

COPY parser.y lexer.l compile.sh run.sh ./

RUN chmod +x compile.sh run.sh && \
    cp compile.sh /usr/local/bin/compile && \
    cp run.sh /usr/local/bin/run