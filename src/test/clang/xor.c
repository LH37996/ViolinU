__asm__("xori $1,$0,0xffff;"
        "xori $2,$0,0x00ff;"
        "xor  $3,$1,$2;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;"
        "nop;");