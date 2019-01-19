/* author: weizhong lai*/
package group32;

import negotiator.AgentID;
import negotiator.Bid;
import negotiator.actions.Accept;
import negotiator.actions.Action;
import negotiator.actions.Offer;
import negotiator.parties.AbstractNegotiationParty;
import negotiator.parties.NegotiationInfo;
import negotiator.utility.UtilitySpace;

import java.util.List;
public class GeneticAlgorithm {
    public int allNumber;
    public double ipop[];
    public double sigma;
    static int ite;
    public GeneticAlgorithm()
    {
        sigma = 1;
        allNumber = 10;
        ipop = new double[10];
        ite = 0;
    }

    public double calculatefitnessvalue(double utility) {
        double phi = 0.2;
        double left = -(utility-sigma)*(utility-sigma)/(2*phi*phi);
        double fitness = Math.exp(left);
        return fitness;
    }
    public void select() {
        ite++;
        double selected[] = new double[allNumber];
        double F = 0;
        double evals[] = new double[allNumber];
        double p[] = new double[allNumber];  //每一个的概率
        double q[] = new  double[allNumber]; //累积和
        for (int i=0;i<10;i++) {
            F = F + calculatefitnessvalue(ipop[i]);   //所有适应值的总和
            evals[i] = calculatefitnessvalue(ipop[i]);  //每个适应值
        }

        for (int i = 0; i < allNumber; i++) {
            p[i] = evals[i] / F;
            if (i == 0)
                q[i] = p[i];
            else {
                q[i] = q[i - 1] + p[i];
            }
        }

        for (int i = 0; i < allNumber; i++) {
            double r = Math.random()*q[allNumber-1];
            if (r <= q[0]) {
                selected[i] = ipop[0];
            } else {
                for (int j = 1; j < allNumber; j++) {
                    if (r < q[j]) {
                        selected[i] = ipop[j];
                        break; //确定区间后跳出循环
                    }
                }
            }
        }
        int i = 0;
        while (i<allNumber)
        {
            ipop[i] = selected[i];
            i = i+1;
        }
    }
    public void mutation()
    {
        double a = Math.random()*6;
        int num = (int)a;
        for (int i=0;i<num;i++)
        {
            ipop[i] = Math.random();
        }
    }
    public String decimal2Binary(double value) throws Exception {
        // 整数部分的值
        int in = (int) value;
        System.out.println("The integer is: " + in);
        // 小数部分的值
        double r = value - in;
        System.out.println("The decimal number is: " + r);

        StringBuilder stringBuilder = new StringBuilder();
        // 将整数部分转化为二进制
        int remainder = 0;
        int quotient = 0;
        while (in != 0) {
            // 得商
            quotient = in / 2;
            // 得余数
            remainder = in % 2;
            stringBuilder.append(remainder);
            in = quotient;
        }
        stringBuilder.reverse();
        stringBuilder.append(".");

        // 将小数部分转化为二进制
        int count = 18; // 限制小数部分位数最多为32位，如果超过32为则抛出异常
        double num = 0;

        while (r > 0.0000000001) {
            count--;
            if (count == 0) {
                return stringBuilder.toString();
            }

            num = r * 2;
            if (num >= 1) {
                stringBuilder.append(1);
                r = num - 1;
            } else {
                stringBuilder.append(0);
                r = num;
            }
        }

        return stringBuilder.toString();
    }
    public double binaToDouble(String _bin)
    {
        double sum=0;
        for(int i=1;i<_bin.length();i++)
        {
            double temp;
            temp = i;
            double right = Math.pow( 2.0, -temp);
            _bin.substring(i,i+1);

            double left = (double)Integer.parseInt(_bin.substring(i,i+1));
            sum = sum + left *right;
        }
        return sum;
    }
    public void cross() {
        String temp1, temp2;
        double ipopTemp[] =new double[10];
        for(int i=0;i<10;i++)
        {
            ipopTemp[i] = ipop[i];
        }

        String _bin[] = new String[10];

        for(int i=0;i<allNumber;i++)
        {
            try {
                _bin[i] = decimal2Binary(ipop[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int GENE = _bin[1].length();
        for (int i = 0; i < allNumber; i++) {
            if (Math.random() < 0.60) {
                int pos = (int)(Math.random()*GENE)+1;     //pos位点前后二进制串交叉
                temp1 = _bin[i].substring(0, pos) + _bin[(i + 1) % allNumber].substring(pos,GENE);
                temp2 = _bin[(i + 1) % allNumber].substring(0, pos) + _bin[i].substring(pos,GENE);
                _bin[i] = temp1;
                _bin[(i + 1) % allNumber] = temp2;
            }
        }
        for(int i=0;i<allNumber;i++)
        {
             binaToDouble(_bin[i]);
        }
    }
}