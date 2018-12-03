package Client;

import javax.swing.JOptionPane;

public class Menu {

    public static void main(String[] args) {
        
        Integer continuar, numConta, numContaTransf, operacao;
        Double valor;
        String resposta[];
        
        do {
            //Valida se a conta existe
            numConta = ValidaConta("Digite o número da conta:", "Conta");
            
            if (numConta == -1) { operacao = 0; }
            else if (numConta != 0) {
                do{
                    operacao = ValidaOperacao("Digite o número da operação\n" 
                            + "1 - Saldo\n"
                            + "2 - Depósito\n"
                            + "3 - Saque\n"
                            + "4 - Transferência\n"
                            + "5 - Extrato\n"
                            + "0 - Sair", "Operação");
                    
                    switch (operacao) {
                        case 0:
                            continuar = 1;
                            break;
                        case -1:
                            JOptionPane.showMessageDialog(null, "Escolha uma operação válida.", "Operação inválida", 0);
                            continuar = 0;
                            break;
                        default:
                            if (operacao >= 2 && operacao <= 4) {
                                valor = ValidaValor();
                            }else { valor = 0.0; }
                            
                            //Pega conta para transferência
                            if (operacao == 4) {
                                do{
                                    numContaTransf = ValidaConta("Digite o número da conta para tranferência:", "Transferência");
                                    
                                    if (numContaTransf == 0){
                                        JOptionPane.showMessageDialog(null, "Digite uma conta existente.", "Conta inválida", 0);
                                        continuar = 1;
                                    }else if (numConta.equals(numContaTransf)) {
                                        JOptionPane.showMessageDialog(null, "Transferência proibida para a mesma conta.", "Conta inválida", 0);
                                        continuar = 1;
                                    }else { continuar = 0; }
                                    
                                }while (continuar == 1);
                            }else { numContaTransf = 0; }
                            
                            //Realiza a operação
                            TCPClient.main(new String[]{operacao.toString(), numConta.toString(), valor.toString(), numContaTransf.toString()});
                            
                            //Resultado da operação
                            resposta = TCPClient.data.split("!");
                            if (resposta[0].equals("OK")) {
                                JOptionPane.showMessageDialog(null, resposta[1], "", 1);
                                continuar = JOptionPane.showConfirmDialog(null, "Deseja fazer outra operação nessa conta?", "Continuar", JOptionPane.YES_NO_OPTION);
                            }else{
                                JOptionPane.showMessageDialog(null, resposta[1], "Erro", 0);
                                continuar = 1;
                            }   break;
                    }
                }while (continuar == 0);
                
            }else{ JOptionPane.showMessageDialog(null, "Digite uma conta existente.", "Conta inválida", 0); operacao = -1; }
            
        } while (operacao != 0);
    }
    
    public static int ValidaConta(String mensagem, String titulo){
        Integer conta;
        String strConta = JOptionPane.showInputDialog(null, mensagem, titulo, 3);
            
        try{
            conta = Integer.parseInt(strConta);
            
            //Verifica se a conta existe
            TCPClient.main(new String[] {"99", strConta, null, null});
            
            if (TCPClient.data.equals("true")){ return conta; }
            else{ return 0; }
            
        }catch(NumberFormatException e){
            if (strConta == null){ return -1; }
            else{ return 0; }
        }
    }
    
    public static int ValidaOperacao(String mensagem, String titulo){
        
        try {
            Integer op;
            
            op = Integer.parseInt(JOptionPane.showInputDialog(null, mensagem, titulo, 3));
            
            if (op < 0 || op > 5) { return -1; }
            else{ return op; }
        } catch (NumberFormatException e) { return -1; }
    }
    
    public static double ValidaValor(){
        
        boolean continuar;
        double valor = 0;
        
        do{
            try { 
                valor = Double.parseDouble(JOptionPane.showInputDialog(null, "Digite o valor:", "Valor", 3));
                continuar = valor < 0;
            }catch (NumberFormatException e) { continuar = true; }
        }while (continuar);
        
        return valor;
    }
}