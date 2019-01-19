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
	public GeneticAlgorithm()
	{
		allNumber = 10;
		ipop = new double[10];
	}

	public double[] calculatefitnessvalue(double utility) {
		double fitness;
        fitness = utility;
        double[] returns = { utility, fitness };
        return returns;
    }
	public void select() {
		double selected[] = new double[allNumber];
		double F = 0;
		double evals[] = new double[allNumber];
		double p[] = new double[allNumber];  //每一个的概率
		double q[] = new  double[allNumber]; //累积和
		for (int i=0;i<10;i++) {
			F = F + calculatefitnessvalue(ipop[i])[1];   //所有适应值的总和
			evals[i] = calculatefitnessvalue(ipop[i])[1];  //每个适应值
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
}