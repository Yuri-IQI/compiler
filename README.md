# Compilador

Compilador completo para uma linguagem de programação simples, implementado em Java com ANTLR4. Realiza as fases de análise léxica, sintática, semântica, geração de código intermediário (3AC), otimização e geração de código Assembly x86 MASM 32-bits.

---

## Requisitos

* Java 21+
* Maven 3.8+
* ANTLR4 4.13.2+
* MASM32 (para montagem e execução nativa do assembly gerado no Windows)

---

## Estrutura do Projeto

```text
src/
├── main/
│   ├── antlr4/
│   │   └── com/inm/antlr4/
│   │       └── Program.g4                                  # Gramática da linguagem
│   └── java/
│       └── com/inm/
│           ├── Main.java                                   # Ponto de entrada
│           ├── compilation/
│           │   ├── CompilationPipeline.java                # Gerenciamento das fases do compilador
│           │   ├── CompilationContext.java                 # Contexto compartilhado entre as fases
│           │   ├── Executor.java                           # Orquestra a fase 5: resolve o workspace, chama montagem, linkagem e execução do .exe gerado
│           │   ├── ProcessExecutor.java                    # Encapsula os comandos ml.exe (MASM) e link.exe do MASM32 SDK com seus flags e caminhos
│           │   └── ProcessRunner.java                      # Executa processos externos via ProcessBuilder com leitura assíncrona de stdout/stderr
│           ├── terminal/
│           │   ├── TerminalHandler.java                    # Interface de terminal e modos de leitura
│           │   └── ExecutionParams.java                    # Parâmetros de execução
│           ├── helper/
│           │   └── ParseHelper.java                        # Criação da árvore sintática
│           ├── semantic/
│           │   ├── SemanticAndIntermediateListener.java    # Análise semântica e geração 3AC
│           │   ├── SymbolTable.java                        # Tabela de símbolos com escopos aninhados
│           │   └── TypedOperand.java                       # Wrapper de valor e tipo semântico
│           └── generator/
│               ├── Optimizer.java                          # Otimizador do código intermediário (3AC)
│               ├── ThreeAddressCode.java                   # Repositório de instruções intermediárias
│               └── assembly/
│                   ├── AssemblyGenerator.java              # Orquestração da geração de assembly
│                   ├── InstructionTranslator.java          # Tradução de 3AC para assembly
│                   ├── InstructionEmitter.java             # Emissão de instruções de baixo nível
│                   ├── HelperInstructions.java             # Rotinas auxiliares (print/read)
│                   └── Writer.java                         # Construtor do arquivo assembly
└── test/
    └── java/
        └── com/inm/
            ├── PipelineTest.java                           # Testes da pipeline de compilação
            └── ExecutionTest.java                          # Testes de execução do assembly gerado
```
---

## Compilar e Executar

### Via Maven

```bat
# compilar e executar em modo SCRIPT (entrada manual)
mvn compile exec:java

# modo TEST: selecionar script dos resources
mvn compile exec:java -Dmode=TEST

# modo FILE: compilar arquivo específico
mvn compile exec:java -Dmode=FILE -DfilePath=scripts/meu-programa.prog

# modo DIR: compilar todos os .prog de uma pasta
mvn compile exec:java -Dmode=DIR -DfilePath=scripts/valid

# exibir árvore sintática em janela gráfica
mvn compile exec:java -Dmode=TEST -DshowTree=true

# compilar e executar o assembly gerado
mvn compile exec:java -Dmode=FILE -DfilePath=meu.prog -Dexec=true

# especificar pasta de saída do assembly
mvn compile exec:java -Dmode=FILE -DfilePath=meu.prog -Doutput=minha-pasta
```

### Via JAR

```bat
# gerar o JAR
mvn package

# modo SCRIPT
java -jar target/compiler.jar

# modo TEST
java -jar target/compiler.jar -Dmode=TEST

# modo FILE com execução
java -jar target/compiler.jar -Dmode=FILE -DfilePath=meu.prog -Dexec=true

# modo DIR
java -jar target/compiler.jar -Dmode=DIR -DfilePath=scripts/valid
```
---

## Parâmetros

| Parâmetro | Valores | Padrão | Descrição |
|---|---|---|---|
| `mode` | `SCRIPT`, `TEST`, `FILE`, `DIR` | `SCRIPT` | Modo de leitura do código fonte |
| `showTree` | `true`, `false` | `false` | Exibe a árvore sintática em janela gráfica |
| `flag` | qualquer string | `#` | Delimitador de fim de entrada no modo SCRIPT |
| `exec` | `true`, `false` | `false` | Monta e executa o assembly gerado após compilar |
| `output` | caminho | `output` | Pasta de saída para os arquivos `.asm` gerados |
| `filePath` | caminho | `./` | Caminho do arquivo ou pasta nos modos FILE e DIR |

---

## Pipeline de Compilação

````
Código Fonte (.prog)
       │
       ▼
[ Léxico + Sintático ] ──────────────► Validação via Gramática ANTLR4
       │
       ▼
[ Semântica + 3AC ] ─────────────────► Checagem de Tipos, Escopos e Emissão Intermediária
       │
       ▼
[ Otimização ] ──────────────────────► Constant Folding/Propagation, Dead Code Elimination
       │
       ▼
[ Geração de Assembly ] ─────────────► Emissão de código MASM x86 32-bits nativo
       │
       ▼
[ Montagem/Execução (Opcional) ] ────► Execução do executável PE final (-Dexec=true)
````