package com.psy888;

public class Game {


    //Game field size 10x10
    static final int FIELD_SIZE = 10;

    UIOut out;

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
        out = new UIOut(userField,compField);
        fillGameField(userField);
        fillGameField(compField);
        out.printField();



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
        for (int i = shipLength; i > 0; i--) { // 4

            //отдельный корабль
            for (int j = 0; j <= shipLength - i; j++) {
               /* System.out.println("Введите координы " +
                        ((j==0)?"начала ":"")+
                        (shipLength-i+1) +
                        " палубного корабля");*/
                //координаты начала нового корабля
                int row;
                int column;
                do {
                    row = (int) (Math.random() * (10 - 1) + 1);
                    column = (int) (Math.random() * (10 - 1) + 1);
                } while (!checkField(row-1, column-1,row+1, column+1, gameField));
//                } while (!isItClear(row, column, gameField));
                System.out.println(isItClear(row, column, gameField) + "  " + gameField[row][column]);
                //длинна текущего корабля
                int curShipLength = shipLength - (shipLength - i);

                //Палубы отдельного корабля
                gameField[row][column] = curShipLength; //начало корабля
                int[] prevMove = new int[]{row, column};

                for (int k = 0; k < curShipLength - 1; k++) {
                    //todo определить следущие координаты
                    int[] nextMove = findNextCoordinates(row, column, gameField);
//                    if (nextMove == null) {
//                        nextMove = findNextCoordinates(prevMove[0], prevMove[1], gameField);
//                    }
                    //save history
                    prevMove[0] = row;
                    prevMove[1] = column;
                    //move cursor
                    row = nextMove[0];
                    column = nextMove[1];

                    gameField[nextMove[0]][nextMove[1]] = curShipLength; //row column
                }

            }
            System.out.println("Ship Length " + (shipLength - (shipLength - i)));
            out.printField();
        }

    }

    /**
     * find next coordinates
     */
    int[] findNextCoordinates(int curRow, int curColumn, int[][] gameField) {

        /*
            Варианты хода
                y--
             x-- 0 x++
                y++

                Варианты (другие корабли)
                |row-2,col-1| row-2,col | row-2,col+1 |
            ---------------------------
     row-1,col-2|row-1,col-1| row-1,col | row-1,col+1| row-1,col+2
     ------------------------------------------
     row,col-2  |row,col-1   |   0       | row,col+1  | row,col+2
     ------------------------------------------
     row+1,col-2|row+1,col-1 | row+1,col | row+1,col+1 | row+1,col+2
     ------------------------------------------
                |row+2,col-1 |  row+2,col  |  row+2,col+1  |

         */

        int[][] result = new int[4][2];
        int chanceCnt = 0;

        int curShipLength = gameField[curRow][curColumn];

        //--------------------------------------------------------------------------
//        if (isUpClear(curRow, curColumn, gameField)) {
        if (checkField(curRow - 2, curColumn - 1, curRow - 1, curColumn + 1, gameField)) {
            try {
                System.out.println(" true " + gameField[curRow - 1][curColumn]);
                result[chanceCnt] = new int[]{curRow - 1, curColumn};//row , col
                chanceCnt++;

            } catch (IndexOutOfBoundsException e) {/*ignore*/}
        }
//        if (isRightClear(curRow, curColumn, gameField)) {
        if (checkField(curRow - 1, curColumn + 1, curRow + 1, curColumn + 2, gameField)) {
            try {
                System.out.println(" true " + gameField[curRow][curColumn+1]);
                result[chanceCnt] = new int[]{curRow, curColumn + 1};
                chanceCnt++;
            } catch (IndexOutOfBoundsException e) {/*ignore*/}
        }
//        if (isDownClear(curRow, curColumn, gameField)) {
        if (checkField(curRow + 1, curColumn - 1, curRow + 2, curColumn + 1, gameField)) {
            try {
                System.out.println(" true " + gameField[curRow+1][curColumn]);
                result[chanceCnt] = new int[]{curRow + 1, curColumn};
                chanceCnt++;
            }catch (IndexOutOfBoundsException e) {/*ignore*/}
        }
//        if (isLeftClear(curRow, curColumn, gameField)) {
        if (checkField(curRow - 1, curColumn - 2, curRow + 1, curColumn - 1, gameField)) {
            try {
                System.out.println(" true " + gameField[curRow][curColumn-1]);
                result[chanceCnt] = new int[]{curRow, curColumn - 1};
                chanceCnt++;
            }catch (IndexOutOfBoundsException e) {/*ignore*/}
        }


        if (chanceCnt > 0) {
            int select = (int) (Math.random() * chanceCnt); //[0,chanceCnt]
//            int select = 0; //[0,chanceCnt]
            return result[select];
        } else {
            return null;
        }
    }

    /**
     * check up from coordinates
     *
     * @param col       - y
     * @param row       - x
     * @param gameField
     * @return
     */
    boolean isUpClear(int row, int col, int[][] gameField) {

        try {
            if (gameField[row - 1][col] == 0) {
                int left;
                int leftUp;
                int up;
                int rightUp;
                int right;
                /*
                |row-2,col-1| row-2,col | row-2,col+1 |
                ---------------------------------------
                |row-1,col-1| row-1,col | row-1,col+1|
                 */
                try {
                    left = gameField[row - 1][col - 1];
                } catch (IndexOutOfBoundsException ex) {
                    left = 0;
                }
                try {
                    leftUp = gameField[row - 2][col - 1];
                } catch (IndexOutOfBoundsException ex) {
                    leftUp = 0;
                }
                try {
                    up = gameField[row - 2][col];
                } catch (IndexOutOfBoundsException ex) {
                    up = 0;
                }
                try {
                    rightUp = gameField[row - 2][col + 1];
                } catch (IndexOutOfBoundsException ex) {
                    rightUp = 0;
                }
                try {
                    right = gameField[row - 1][col + 1];
                } catch (IndexOutOfBoundsException ex) {
                    right = 0;
                }


                if (
                        left == 0 &&
                                leftUp == 0 &&
                                up == 0 &&
                                rightUp == 0 &&
                                right == 0
                ) {
                    return true;
//                    result[chanceCnt++] = new int[]{column, row - 1};
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    /**
     * check right of coordinates
     *
     * @param col
     * @param row
     * @param gameField
     * @return
     */
    boolean isRightClear(int row, int col, int[][] gameField) {
        try {
            if (gameField[row][col + 1] == 0) {
                int up;
                int rightUp;
                int right;
                int rightDown;
                int down;
                /*
                | row-1,col+1| row-1,col+2
                ----------------------------
                | row,col+1  | row,col+2
                -------------------------
                | row+1,col+1 | row+1,col+2
                 */
                try {
                    up = gameField[row - 1][col + 1];
                } catch (IndexOutOfBoundsException ex) {
                    up = 0;
                }
                try {
                    rightUp = gameField[row - 1][col + 2];
                } catch (IndexOutOfBoundsException ex) {
                    rightUp = 0;
                }
                try {
                    right = gameField[row][col + 2];
                } catch (IndexOutOfBoundsException ex) {
                    right = 0;
                }
                try {
                    rightDown = gameField[row + 1][col + 2];
                } catch (IndexOutOfBoundsException ex) {
                    rightDown = 0;
                }
                try {
                    down = gameField[row + 1][col + 1];
                } catch (IndexOutOfBoundsException ex) {
                    down = 0;
                }

                if (
                        up == 0 &&
                                rightUp == 0 &&
                                right == 0 &&
                                rightDown == 0 &&
                                down == 0
                ) {
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    boolean checkField(int rowStart, int colStart, int rowEnd, int colEnd, int[][] gameField) {
        for (int i = rowStart; i <= rowEnd; i++) { //rows
            for (int j = colStart; j <= colEnd; j++) {
                try {
                    if (gameField[i][j] != 0) {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ex) {
                    /*ignore*/
                }
            }
        }
        return true;
    }

    /**
     * Check down of coordinates
     *
     * @param col
     * @param row
     * @param gameField
     * @return
     */
    boolean isDownClear(int row, int col, int[][] gameField) {
        try {
            if (gameField[row + 1][col] == 0) {
                int right;
                int rightDown;
                int down;
                int leftDown;
                int left;
/*
            |row+1,col-1    | row+1,col | row+1,col+1 |
                -----------------------------------
            | row+2,col-1 |  row+2,col  |  row+2,col+1
 */
                try {
                    right = gameField[row + 1][col + 1];
                } catch (IndexOutOfBoundsException e) {
                    right = 0;
                }
                try {
                    rightDown = gameField[row + 2][col + 1];
                } catch (IndexOutOfBoundsException e) {
                    rightDown = 0;
                }
                try {
                    down = gameField[row + 2][col];
                } catch (IndexOutOfBoundsException e) {
                    down = 0;
                }
                try {
                    leftDown = gameField[row + 2][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    leftDown = 0;
                }
                try {
                    left = gameField[row + 1][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    left = 0;
                }

                if (
                        right == 0 &&
                                rightDown == 0 &&
                                down == 0 &&
                                leftDown == 0 &&
                                left == 0
                ) {
//                    result[chanceCnt++] = new int[]{column, row + 1};
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        return false;
    }

    /**
     * Check left of coordinates
     *
     * @param col
     * @param row
     * @param gameField
     * @return
     */
    boolean isLeftClear(int row, int col, int[][] gameField) {
        try {
            if (gameField[row][col - 1] == 0) {
                int down;
                int leftDown;
                int left;
                int leftUp;
                int up;
                /*
                row-1,col-2|row-1,col-1|
                -------------------------
                row,col-2  |row,col-1   |0
                -------------------------
                row+1,col-2|row+1,col-1 |
                 */
                try {
                    down = gameField[row + 1][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    down = 0;
                }
                try {
                    leftDown = gameField[row + 1][col - 2];
                } catch (IndexOutOfBoundsException e) {
                    leftDown = 0;
                }
                try {
                    left = gameField[row][col - 2];
                } catch (IndexOutOfBoundsException e) {
                    left = 0;
                }
                try {
                    leftUp = gameField[row - 1][col - 2];
                } catch (IndexOutOfBoundsException e) {
                    leftUp = 0;
                }
                try {
                    up = gameField[row - 1][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    up = 0;
                }

                if (
                        down == 0 &&
                                leftDown == 0 &&
                                left == 0 &&
                                leftUp == 0 &&
                                up == 0
                ) {
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        return false;
    }

    boolean isItClear(int row, int col, int[][] gameField) {
        try {
            if (gameField[row][col] == 0) {
                int leftDown;
                int left;
                int leftUp;
                int up;
                int rightUp;
                int right;
                int rightDown;
                int down;
                try {
                    leftDown = gameField[row + 1][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    leftDown = 0;
                }
                try {
                    left = gameField[row][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    left = 0;
                }
                try {
                    leftUp = gameField[row - 1][col - 1];
                } catch (IndexOutOfBoundsException e) {
                    leftUp = 0;
                }
                try {
                    up = gameField[row - 1][col];
                } catch (IndexOutOfBoundsException e) {
                    up = 0;
                }
                try {
                    rightUp = gameField[row - 1][col + 1];
                } catch (IndexOutOfBoundsException e) {
                    rightUp = 0;
                }
                try {
                    right = gameField[row][col + 1];
                } catch (IndexOutOfBoundsException e) {
                    right = 0;
                }
                try {
                    rightDown = gameField[row + 1][col + 1];
                } catch (IndexOutOfBoundsException e) {
                    rightDown = 0;
                }
                try {
                    down = gameField[row + 1][col];
                } catch (IndexOutOfBoundsException e) {
                    down = 0;
                }


                if (leftDown == 0 &&
                        left == 0 &&
                        leftUp == 0 &&
                        up == 0 &&
                        rightUp == 0 &&
                        right == 0 &&
                        rightDown == 0 &&
                        down == 0
                ) {
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            return false;
        }
        return false;
    }


}
