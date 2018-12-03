package Server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Banco {
    
    protected ArrayList<Conta> listaContas;
    protected Random valor = new Random();
    protected DecimalFormat df = new DecimalFormat("#.##");

    protected Banco(int totalContas){
        listaContas = new ArrayList();
        
        for (int i = 1; i <= totalContas; i++) {
            Conta conta = new Conta();
            conta.numConta = (i * 100);
            conta.saldo = Double.parseDouble(df.format(valor.nextDouble() * 500).replace(",", ".")); //Valor inicial entre 0 e 500
            //conta.movimentacao.add("Saldo Inicial - R$ " + conta.saldo);
            listaContas.add(conta);
        }
        listaContas.forEach((item)-> { System.out.println("Conta: " + item.numConta + " - Saldo: R$ " + item.saldo); });
    }
}