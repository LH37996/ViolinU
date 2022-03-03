PREFIX != if [ -f "/etc/arch-release" ]; then \
		echo mips-elf; \
	else \
	  echo mips-linux-gnu; \
  fi

TARGET=xor.hexS xor.hex xor_load.hex

SRC=src/test/clang
DIR=target/clang
BCC=gcc
SCC=${PREFIX}-gcc
RCC=${PREFIX}-gcc
MCP=${PREFIX}-objcopy

# generate asm code for mips
${DIR}/%.asm: ${SRC}/%.c
	${SCC} -O1 -S -o $@ $^

# generate executable binary code
${DIR}/%.exe: ${SRC}/%.c
	${BCC} -o $@ $^

# generate raw binary code for mips
${DIR}/%.bin: ${DIR} ${SRC}/%.c
	${RCC} -O1 -o ${DIR}/$*.o -c $^
	${MCP} -O binary -j .text ${DIR}/$*.o $@

${DIR}/%.binS: ${DIR} ${SRC}/%.S
	${RCC} -O1 -o ${DIR}/$*.oS -c $^
	${MCP} -O binary -j .text ${DIR}/$*.oS $@

${DIR}/%.hexS: ${DIR}/%.binS
	hexdump -ve '4/1 "%02x"' -e '"\n"' $^ > $@

# generate hex file for mips
${DIR}/%.hex: ${DIR}/%.bin
	hexdump -ve '4/1 "%02x"' -e '"\n"' $^ > $@

# debug
${DIR}/%.debug: ${DIR}/%.bin
	mips-linux-gnu-objdump -d $^.bin
	hexdump -C $^

all: ${DIR} $(addprefix target/clang/,$(TARGET))

${DIR}:
	mkdir -p ${DIR}

clean:
	rm -rf ${DIR}