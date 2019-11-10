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
    0       - пусто

    1       - однопалубный  (целый)
    2,2     - двухпалубный (целый)
    3,3,3   - трехпалубный (целый)
    4,4,4,4 - четырехпалубный (целый)

    -1       - однопалубный  (убит)
    -2,2     - двухпалубный (подбит)
    -3,3,3   - трехпалубный (подбит)
    -4,-4,4,4 - четырехпалубный (подбит)
     */

    boolean isUserTurn = true; //чей ход

    //constructor
    public Game() {
        //todo fill game fields


    }

    /**
     * print game fields to console
     */
    public void printField() {
    }

    /**
     * make Shot
     */
    public void shot(int[] coordinates) {

        //todo: добавить проверку выиграша
        isUserTurn = !isUserTurn; // смена хода после выстрела

    }

    /**
     * parse user input
     */
    public int[] parseUserInput(String str) {
        return new int[2]; //x,y (0-line,1-column)
    }

    /**
     * fill game field
     */
    public void fillGameField(int[][] gameField) {
        // 4 палубы - 1 шт 4-4 => 0
        // 3 палубы - 2 шт 4-3 => 0,1
        // 2 палубы - 3 шт 4-2 => 0,1,2
        // 1 палуба - 4 шт 4-1 => 0,1,2,3

        int shipLength = 4;
        //все корабли
        for (int i = shipLength; i > 0; i--) {

            //отдельный корабль
            for (int j = 0; j <= shipLength - i; j++) {
               /* System.out.println("Введите координы " +
                        ((j==0)?"начала ":"")+
                        (shipLength-i+1) +
                        " палубного корабля");*/
                //координаты начала
                int x = (int) (Math.random() * (10 - 1) + 1);
                int y = (int) (Math.random() * (10 - 1) + 1);
                //длинна текущего корабля
                int curShipLength = shipLength - (shipLength - i);
                //Палубы отдельного корабля
                for (int k = 0; k < curShipLength; k++) {
                    gameField[x][y] = curShipLength;
                    //todo определить следущие координаты
                    int[] nextMove = findNextCoordinates(x,y,gameField);
                }

            }
        }

    }

    /**
     * find next coordinates
     */
    int[] findNextCoordinates(int curX, int curY, int[][] gameField) {

        /*
            Варианты хода
                y--
             x-- 0 x++
                y++

                Варианты (другие корабли)
            |x-1,y-2| x,y-2 | x+1,y-2 |
            ---------------------------
     x-2,y-1|x-1,y-1| x,y-1 | x+1,y-1 | x+2,y-1
     ------------------------------------------
     x-2,y  |x-1,y  |   0   | x+1,y   | x+2,y
     ------------------------------------------
     x-2,y+1|x-1,y+1| x,y+1 | x+1,y+1 | x+2,y+1
     ------------------------------------------
            |x-1,y+2| x,y+2 | x+1,y+2 |

         */
        int chanceCnt = 4;
        int[][] result = new int[4][2]; //4 варианта по 2 координаты
        int curShipLength = gameField[curX][curY];
        //[x+1] [y] result index 0
        if(gameField[curX+1][curY]==0){

        }


    }


}
