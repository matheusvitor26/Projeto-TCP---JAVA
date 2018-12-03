package Server;

import java.util.ArrayList;

public class Conta {
    public Integer numConta;
    public Double saldo;
    public String extrato = "";
    public ArrayList<String> movimentacao = new ArrayList();
    
    protected double getSaldo(){
        return saldo;
    }
    
    protected void setSaldo(double valor){
        this.saldo = this.saldo + valor;
    }
    
    protected String getExtrato() {
    	return this.extrato;
    }
    
    protected void setExtrato() {
    	this.extrato = "";
    }
    
    protected void setExtrato(String item) {
    	this.extrato = item;
    }
}