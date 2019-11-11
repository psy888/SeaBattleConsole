package com.psy888;

public class UIOut {
    //Field UI
    static final char CHAR_SHIP = 'O';
    static final char CHAR_SEA = '.';
    static final char CHAR_MISS = '*';
    static final char CHAR_HIT = '+';
    static final char CHAR_KILL = 'x';

    static final String[] letters = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    int[][] user;
    int[][] comp;

    public UIOut(int[][] user, int[][] comp) {
        this.user = user;
        this.comp = comp;
    }

    /**
     * print game fields to console
     */
    public void printField() {
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("\n");
        for (int i = -1; i < user.length; i++) {
            for (int j = -1; j < user.length; j++) {
                if (i >= 0 && j >= 0) {
                    System.out.print(" " + getOutChar(user[i][j], true) + " ");
                } else if (i < 0 && j >= 0) {
                    System.out.print(" " + (j+1) + " ");
                } else if (i >= 0 && j < 0) {
                    System.out.print(" " + letters[i] + " ");
                }else{
                    System.out.print("   ");
                }
            }
            if (i >= 0) {
                System.out.print("\t" + letters[i] + "\t");
            }else {
                System.out.print("\t \t");
            }
            for (int j = 0; j <= comp.length; j++) {
                if (i >= 0 && j != comp.length) {
                    System.out.print(" " + getOutChar(comp[i][j], false) + " ");
                } else if (i < 0 && j > 0) {
                    System.out.print(" " + j + " ");
                } else if (i >= 0 && j == comp.length) {
                    System.out.print(" " + letters[i] + " ");
                }
            }
            System.out.println();
        }

    }

    /**
     * get output char
     * @param i - gameFieldVal
     * @param isUser - is user turn
     * @return
     */
    String getOutChar(int i, boolean isUser) {
        if (i > 0 && i < 5) {
            return "" + ((isUser) ? CHAR_SHIP : CHAR_SEA);
        } else if (i < 0 && i > -5) {
            return "" + CHAR_HIT;
        } else if (i == 0) {
            return "" + CHAR_SEA;
        } else if (i < -4) {
            return "" + CHAR_KILL;
        } else {
            return "" + CHAR_MISS;
        }
    }

    /**
     * print game message
     * @param str
     */
    void msg(String str){
        System.out.println(str);
    }
}
