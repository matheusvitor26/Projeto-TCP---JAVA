package Server;

import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Connection extends Thread {
    
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");
    private ArrayList<Conta> listaContas;
    private Criptografia TEA = new Criptografia();
    private DataInputStream in;
    private DataOutputStream out;
    private Socket clientSocket;
        
    public Connection (Socket aClientSocket, ArrayList Contas) {

        try {
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream());
            out = new DataOutputStream( clientSocket.getOutputStream());
            listaContas = Contas;
            this.start();
        } catch (IOException e) {System.out.println("Connection:"+e.getMessage());}
    }

    @Override
    public void run(){
        
        try {
            String[] data = TEA.decrypt(in.readUTF()).split("#");
            Integer op = Integer.parseInt(data[0]);
            Integer conta = Integer.parseInt(data[1]);
            Double valor = 0.0;
            
            switch (op){
                case 99:
                    ValidaConta(conta);
                    break;
                case 1:
                    Saldo(conta);
                    break;
                case 2:
                    valor = Double.parseDouble(data[2]);
                    Deposito(conta, valor);
                    break;
                case 3:
                    valor = Double.parseDouble(data[2]);
                    Saque(conta, valor);
                    break;
                case 4:
                    valor = Double.parseDouble(data[2]);
                    Integer contaDest = Integer.parseInt(data[3]);
                    Transferencia(conta, valor, contaDest);
                    break;
                case 5:
                    Extrato(conta);
                    break;
                default:
                    out.writeUTF(TEA.encrypt("Operação não identificada."));
                    break;
            }
        } catch (EOFException e) {System.out.println("EOF:"+e.getMessage());
        } catch (IOException e) {System.out.println("readline:"+e.getMessage());
        } finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
    }
    
    private void ValidaConta(Integer numConta) throws IOException{
        boolean isConta = false;
        try {
            isConta = listaContas.stream().anyMatch((item) -> (item.numConta.equals(numConta)));
            out.writeUTF(TEA.encrypt(String.valueOf(isConta)));
        } catch (IOException e) { out.writeUTF(TEA.encrypt(String.valueOf(isConta))); }
    }
    
    private void Saldo(Integer numConta){
        listaContas.forEach((c)-> { if (c.numConta.equals(numConta)){ try {
                out.writeUTF(TEA.encrypt("OK!Saldo: R$ " + String.valueOf(c.getSaldo())));
            } catch (IOException ex) { System.out.println("readline:"+ex.getMessage()); }
        }});
    }
    
    private void Deposito(int numConta, double valor) throws IOException{
        try {
            listaContas.forEach((c)-> { if (c.numConta.equals(numConta)) {
                c.setSaldo(valor);
                c.movimentacao.add(df.format(new Date()) + String.format("%-20s", "    Depósito") + String.format("%12s", valor));
            }});
            out.writeUTF(TEA.encrypt("OK!Depósito realizado com sucesso."));
        } catch (IOException e) { out.writeUTF(TEA.encrypt("ERRO!Erro ao realizar depósito.")); }
    }

    private void Saque(int numConta, double valor) {
        
        listaContas.forEach((c)-> { if (c.numConta.equals(numConta)) {
            if (c.getSaldo() - valor >= 0) {
                try {
                    c.setSaldo(valor * -1);
                    c.movimentacao.add(df.format(new Date()) + String.format("%-20s", "    Saque") + String.format("%12s", valor * -1));
                    out.writeUTF(TEA.encrypt("OK!Saque realizado com sucesso."));
                }catch (IOException e){ System.out.println("Erro ao realizar saque."); }
            }else {
                try { out.writeUTF(TEA.encrypt("ERRO!Saldo insulficiente.")); }
                catch (IOException e) { System.out.println("Erro ao realizar saque."); }
        }}});
    }

    private void Transferencia(int contaOri, double valor, int contaDest){
        
        listaContas.forEach((c)-> { if (c.numConta.equals(contaOri)) {
            if (c.getSaldo() - valor >= 0) {
                try {
                    c.setSaldo(valor * -1);
                    c.movimentacao.add(df.format(new Date()) + String.format("%-20s", "    Transferência") + String.format("%12s", valor * -1));
                    listaContas.forEach((t)-> { if (t.numConta.equals(contaDest)) { 
                        t.setSaldo(valor); 
                        t.movimentacao.add(df.format(new Date()) + String.format("%-20s", "    Transferência") + String.format("%12s", valor));
                    }});
                    out.writeUTF(TEA.encrypt("OK!Transferência realizada com sucesso."));
                }catch (IOException e){ System.out.println("Erro ao realizar transferência."); }
            }else {
                try { out.writeUTF(TEA.encrypt("ERRO!Saldo insulficiente.")); }
                catch (IOException e) { System.out.println("Erro ao realizar transferência."); }
        }}});
    }
    
    private void Extrato(int numConta){
        
        try {
            listaContas.forEach((c)-> { if (c.numConta.equals(numConta)) {
            	c.setExtrato();
            	String extrato;
            	extrato = "Extrato da conta: " + numConta + "\n";
            	extrato += "   Data      Hora       Lançamento" + String.format("%19s", "Valor(R$)") + "\n";	
            	c.setExtrato(c.getExtrato() + extrato);
                c.movimentacao.forEach((item)-> c.setExtrato(c.getExtrato() + item + "\n"));
            }});
            
            listaContas.forEach((a)-> { if (a.numConta.equals(numConta)) {
            	try { out.writeUTF(TEA.encrypt("OK!" + a.getExtrato())); }
                catch (IOException io) {}
            }});
        } catch (Exception e) { System.out.println("Erro ao realizar extrato."); } 
    }
}