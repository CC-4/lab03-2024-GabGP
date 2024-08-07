/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        /*To see better what the program is doing:
        System.out.println("Position: " + this.next + " Token: " + this.tokens.get(this.next) + " Checking id: " + id 
        + " Does it match? " + Boolean.toString(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)));    
        */
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            
            if (id == Token.NUMBER) {
				// Encontramos un numero
				// Debemos guardarlo en el stack de operandos
				operandos.push( this.tokens.get(this.next).getVal() );

			} else if (id == Token.SEMI) {
				// Encontramos un punto y coma
				// Debemos operar todo lo que quedo pendiente
				while (!this.operadores.empty()) {
					popOp();
				}
				
			} else {
				// Encontramos algun otro token, es decir un operador
				// Lo guardamos en el stack de operadores
				// Que pushOp haga el trabajo, no quiero hacerlo yo aqui
				pushOp( this.tokens.get(this.next) );
			}
			

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
        	case Token.PLUS:
        		return 1;
            case Token.MINUS:
                return 1;
        	case Token.MULT:
        		return 2;
            case Token.DIV:
                return 2;
            case Token.MOD:
                return 2;
            case Token.EXP:
                return 3;
            case Token.UNARY:
                return 4;
            case Token.LPAREN:
                return 5;
            case Token.RPAREN:
                return 5;
        	default:
        		return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("suma " + a + " + " + b);
        	this.operandos.push(a + b);
        } else if(op.equals(Token.MINUS)) {
            double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("minus " + a + " - " + b);
        	this.operandos.push(a - b);
        } else if (op.equals(Token.MULT)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mult " + a + " * " + b);
        	this.operandos.push(a * b);
        } else if (op.equals(Token.DIV)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("div " + a + " / " + b);
        	this.operandos.push(a / b);
        } else if (op.equals(Token.MOD)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("mod " + a + " % " + b);
        	this.operandos.push(a % b);
        } else if (op.equals(Token.EXP)) {
        	double a = this.operandos.pop();
        	double b = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("exp " + a + " ^ " + b);
        	this.operandos.push(a ^ b);
        } else if (op.equals(Token.UNARY)) {
        	double a = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("unary " + " ~ " + a);
        	this.operandos.push(~a);
        } else if (op.equals(Token.LPAREN)) {
        	double a = this.operandos.pop();
        	// print para debug, quitarlo al terminar
        	System.out.println("lparen " + "(" + a);
        	this.operandos.push(a);
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        /* Casi todo el codigo para esta seccion se vera en clase */
    	
    	// Si no hay operandos automaticamente ingresamos op al stack

    	// Si si hay operandos:
    		// Obtenemos la precedencia de op
            int curr = pre(op);
        	// Obtenemos la precedencia de quien ya estaba en el stack
            int inStack = pre( "peek" );
        	// Comparamos las precedencias y decidimos si hay que operar
            while (curr <= inStack && true) {
                popOp();
            }

            this.operadores.push(op);
        	// Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
        	// Al terminar operaciones pendientes, guardamos op en stack

    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        int save = next;
        this.next = save;

        next = save;
        if (E1()) { return true; }

        next = save;
        if (E2()) { return true; }

        next = save;
        if (E3()) { return true; }

        next = save;
        if (E4()) { return true; }

        next = save;
        if (E5()) { return true; }

        next = save;
        if (E6()) { return true; }

        next = save;
        if (E7()) { return true; }

        next = save;
        if (E8()) { return true; }

        next = save;
        if (N()) { return true; }

        return false;
    }

    //N + E
    private boolean E1() {
        return N() && term(Token.PLUS) && E();
    }
    //N - E
    private boolean E2() {
        return N() && term(Token.MINUS) && E();
    }
    //N * E
    private boolean E3() {
        return N() && term(Token.MULT) && E();
    }
    //N / E
    private boolean E4() {
        return N() && term(Token.DIV) && E();
    }
    //N % E
    private boolean E5() {
        return N() && term(Token.MOD) && E();
    }
    //N ^ E
    private boolean E6() {
        return N() && term(Token.EXP) && E();
    }
    //- E
    private boolean E7() {
        return term(Token.UNARY) && E();
    }
    //(E)
    private boolean E8() {
        return term(Token.LPAREN) && E() && term(Token.RPAREN);
    }
    
    private boolean N() {
        return term(Token.NUMBER);
    }
    /* TODO: sus otras funciones aqui */
}
