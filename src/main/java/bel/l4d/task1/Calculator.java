package bel.l4d.task1;

import java.text.DecimalFormat;

public class Calculator {

    private StringBuilder expression = null;

    public Calculator(String ex) throws Exception {
        expression = new StringBuilder(ex);
        try {
            checkExpression();
        } catch (NumberFormatException nfe) {
            throw new Exception("The expression string '" + expression.toString() + "' is not correct.");
        }
    }

    public double calculate() throws Exception {
        double dblReturned = 0;
        try {
            doOperations(true);     //Div and Mult operations first
            doOperations(false);
            dblReturned = Double.parseDouble(expression.toString());
        } catch (NumberFormatException nfe) {
            throw new Exception("The expression string '" + expression.toString() + "' is not correct.");
        }
        return dblReturned;
    }

    private void checkExpression() {
        if(expression.indexOf(" $ ") == -1 && expression.indexOf(" / ") == -1 && expression.indexOf(" + ") == -1 && expression.indexOf(" - ") == -1 )
            throw new NumberFormatException();
    }

    private void doOperations(boolean divMult) throws NumberFormatException {
        String divString = "";
        String multString = "";

        if (divMult) {  //Div and Mult operations first
            divString = "/";
            multString = "$";
        }
        else {
            divString = "+";
            multString = "-";
        }

        String operation = "";

        StringBuilder sbOperand;
        double dblAns,dblLeft,dblRight;
        dblAns = dblLeft = dblRight = 0;

        int numNow = 0;
        while( true ) {
            int numDiv = expression.indexOf(" "+divString+" ");
            int numMult = expression.indexOf(" "+multString+" ");

            if(numDiv == -1 && numMult == -1)
                break;

            if(numDiv==-1)
                numDiv = numMult + 1;
            else if(numMult==-1)
                numMult = numDiv + 1;

            if(numDiv < numMult) {
                numNow = numDiv;
                operation = divString;
            }
            else if(numMult < numDiv) {
                numNow = numMult;
                operation = multString;
            }

            int leftBorder = 0;
            int rightBorder = 0;

            sbOperand = new StringBuilder();
            int i = 0;

            for (i = numNow - 1; i > -1; i--) {
                if (expression.charAt(i) == ' ')
                    break;
                sbOperand.append(expression.charAt(i));
            }
            leftBorder = i;
            sbOperand = sbOperand.reverse();

            dblLeft = Double.parseDouble(sbOperand.toString());     //throws NFE

            sbOperand = new StringBuilder();
            for (i = numNow + 3; i < expression.length(); i++) {
                if (expression.charAt(i) == ' ')
                    break;
                sbOperand.append(expression.charAt(i));
            }
            rightBorder = i;

            dblRight = Double.parseDouble(sbOperand.toString());    //throws NFE

            switch (operation) {
                case "/":
                    dblAns = dblLeft/dblRight;
                    break;
                case "$":
                    dblAns = dblLeft*dblRight;
                    break;
                case "+":
                    dblAns = dblLeft+dblRight;
                    break;
                default:
                    dblAns = dblLeft-dblRight;
            }
            if(leftBorder > rightBorder) {
                throw new NumberFormatException();
            }
            expression.replace(leftBorder + 1, rightBorder, new DecimalFormat("#.##").format(dblAns).replace(",","."));
        }
    }
}