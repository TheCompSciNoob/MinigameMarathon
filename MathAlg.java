package com.example.per6.startmenufragments;

/**
 * Created by per6 on 10/26/17.
 */

public class MathAlg {
    int ans, num1, num2, num3, num4, roperator, rand1, rand2;
    //1+
    //2-
    //2*
    //4/
    public int genRandMum()
    {
        return (int)(Math.random()*9+1);
    }
    public int genRand4()
    {
        return (int)(Math.random()*4+1);
    }
    public void setStuffs()
    {
        num1=genRandMum();
        num2=genRandMum();
        num3=genRandMum();
        num4=genRandMum();
        roperator=genRand4();
        rand1=genRand4();
        rand2=genRand4();

        if (rand1==1)
        {
            rand1=num1;
        }
        else if (rand1==2)
        {
            rand1=num2;
        }
        else if (rand1==3)
        {
            rand1=num3;
        }
        else if (rand1==4)
        {
            rand1=num4;
        }

        if (rand2==1)
        {
            rand2=num1;
        }
        else if (rand2==2)
        {
            rand2=num2;
        }
        else if (rand2==3)
        {
            rand2=num3;
        }
        else if (rand2==4)
        {
            rand2=num4;
        }

        if (roperator==1)
        {
            ans= rand1+rand2;
        }
        else if (roperator==2)
        {
            ans=Math.abs(rand1=rand2) ;
        }
        else if (roperator==3)
        {
            ans=rand1*rand2;
        }
        else if (roperator==4)
        {
            ans=(int)(rand1/rand2);
        }

    }
    public boolean check(int n1,int n2,int optr)
    {
        if (optr==1)
        {
            if(ans==rand1+rand2)
            {return true;}
            else {return false;}
        }
        else if (optr==2)
        {
            if(ans==rand1-rand2)
            {return true;}
            else {return false;}
        }
        else if (optr==3)
        {
            if(ans==rand1*rand2)
            {return true;}
            else {return false;}
        }
        else if (optr==4)
        {
            if(ans==rand1/rand2)
            {return true;}
            else {return false;}
        }
        return false;
    }

}
