package com.psy888;

public class Game {
    //Field UI
    static final char CHAR_SHIP = 'o';
    static final char CHAR_SEA = '.';
    static final char CHAR_MISS = '-';
    static final char CHAR_HIT = '+';
    static final char CHAR_KILL = 'x';

    //Game field size 10x10
    static final int FIELD_SIZE = 10;

    //Game field ARRAY
    int[][] userField = new int[FIELD_SIZE][FIELD_SIZE];
    int[][] compField = new int[FIELD_SIZE][FIELD_SIZE];

    /*
    10          - однопалубный  (целый)
    20,20       - двухпалубный (целый)
    30,30,30    - трехпалубный (целый)
    40,40,40,40 - четырехпалубный (целый)

    11          - однопалубный  (убит)
    21,20       - двухпалубный (подбит)
    31,30,30    - трехпалубный (подбит)
    41,40,40,40 - четырехпалубный (подбит)
     */

    boolean isUserTurn = true; //чей ход

    //constructor
    public Game (){
        //todo fill game fields


    }

    /**
        print game fields to console
     */
    public void printField(){
    }

    /**
     * make Shot
     */
    public void shot(int[] coordinates){

        //todo: добавить проверку выиграша
        isUserTurn = !isUserTurn; // смена хода после выстрела

    }

    /**
     * parse user input
     */
    public int[] parseUserInput(String str){
        return new int[2]; //x,y (0-line,1-column)
    }

    /**
     * fill game field
     */
    public void fillGameField(){
        // 4 палубы - 1 шт 4-4 => 0
        // 3 палубы - 2 шт 4-3 => 0,1
        // 2 палубы - 3 шт 4-2 => 0,1,2
        // 1 палуба - 4 шт 4-1 => 0,1,2,3

        int shipLength = 4;
        //все корабли
        for (int i = shipLength; i > 0; i--) {

            //отдельный корабль
            for (int j = 0; j <= shipLength-i; j++) {
               /* System.out.println("Введите координы " +
                        ((j==0)?"начала ":"")+
                        (shipLength-i+1) +
                        " палубного корабля");
*/

            }
        }

    }


}
