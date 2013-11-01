package com.antics.ant;


import com.antics.data.Board;

/*
 * An abstract class for outlining a common Ant
 */

public abstract class Ant {
    private static final int GATHERER = 1;
    private static final int BUILDER = 0;
    public int health, hunger, antType, positionX, positionY, lastX, lastY;
    public boolean hasFood;
    public boolean hasDirt;
    Board board;

    public Ant(Board board){
        this.board = board;
    }
    // Ant decision maker
    public abstract boolean action();

    // No pheromone to follow, wander
    public abstract void wander();

    // If user requests to return the ants to nest
    public void reset() {
        move(board.NEST_X, board.NEST_Y);
    }

    // Move Ant
    public void move(int moveX, int moveY) {
        if (board.tiles[moveX][moveY].dug()) {
            // Store previous location3D
            lastX = positionX;
            lastY = positionY;

            // If Gatherer
            if (antType == GATHERER) {
                // If tile doesn't have food in it
                if (!tileHasFood(positionX, positionY)) {
                    // Drop pheromone
                    if (hasFood) {
                        foodPheromone();
                    } else {
                        searchPheromone();
                    }
                }
            }
            //If Builder
            else if (antType == BUILDER) {
                // If has dirt, drop digging pheromone
                // If not, drop searching pheromone
                if (hasDirt)
                    digPheromone();
                else
                    searchPheromone();
            }

            // Move the Ant
            board.tiles[lastX][lastY].antVacateTile(antType());
            positionX = moveX;
            positionY = moveY;
            board.tiles[moveX][moveY].newAnt(antType());
        }
    }

    private boolean tileHasFood(int x, int y) {
        return board.tiles[x][y].hasFood();
    }

    // So renderer knows what to draw
    public int antType() {
        return antType;
    }

    // Check if the Ant is hungry
    public boolean hungry() {
        return hunger < 20;
    }

    // Return whether Ant has dirt or not
    public boolean hasDirt() {
        return hasDirt;
    }

    // LastX co-ordinate
    public int getLastYPosition() {
        return lastX;
    }

    // LastY co-ordinate
    public int getLastXPosition() {
        return lastY;
    }

    // X co-ordinate
    public int getPositionX() {
        return positionX;
    }

    // Y co-ordinate
    public int getPositionY() {
        return positionY;
    }

    // Return hunger levels
    public int hungerLevel() {
        return hunger;
    }

    // Check if currently standing on food
    public boolean foundFood() {
        return tileHasFood(positionX, positionY);
    }

    // Return getHealth of the ant
    public int getHealth() {
        return health;
    }

    // Return whether ant has food or not
    public boolean hasFood() {
        return hasFood;
    }

    // kill ant
    public void die() {
        dropFood();
        board.tiles[positionX][positionY].antVacateTile(antType);
        board.dead++;
    }

    // Check surrounding tiles for food
    public boolean checkForFood() {
        // Left
        if (checkForFoodAndMove(positionX, positionY - 1)) return true;

        // Right
        if (checkForFoodAndMove(positionX, positionY + 1)) return true;

        if (positionX % 2 == 0) {

            if (positionY > 0) {
                // Top Left
                if (checkForFoodAndMove(positionX - 1, positionY - 1)) return true;

                // Bot Left
                if (checkForFoodAndMove(positionX + 1, positionY - 1)) return true;

            }

            // Top Right
            if (checkForFoodAndMove(positionX - 1, positionY)) return true;


            // Bot Right
            if (checkForFoodAndMove(positionX + 1, positionY)) return true;

        } else {
            // Top Left
            if (checkForFoodAndMove(positionX - 1, positionY)) return true;

            // Bot Left
            if (checkForFoodAndMove(positionX + 1, positionY)) return true;

            if (positionY < board.width - 1) {
                // Top Right
                if (checkForFoodAndMove(positionX - 1, positionY + 1)) return true;

                // Bot Right
                if (checkForFoodAndMove(positionX + 1, positionY + 1)) return true;
            }
        }
        return false;
    }

    private boolean checkForFoodAndMove(int x, int y) {
        if (board.inBounds(x, y)
                && tileHasFood(x, y)) {
            if (board.NEST_X != x || board.NEST_Y != y) {
                move(x, y);
                return true;
            }
        }
        return false;
    }

    // Check surrounding tiles for trail
    public boolean smellFoodPheromone() {
        // Left
        if (positionY > 0 && board.tiles[positionX][positionY - 1] != null
                && board.tiles[positionX][positionY - 1].hasFoodPheromone())
            return true;

        // Right
        if (positionY < board.width - 1 && board.tiles[positionX][positionY + 1] != null
                && board.tiles[positionX][positionY + 1].hasFoodPheromone())
            return true;

        if (positionX % 2 == 0) {
            if (positionY > 0) {
                // Top Left
                if (positionX > 0 && board.tiles[positionX - 1][positionY - 1] != null
                        && board.tiles[positionX - 1][positionY - 1].hasFoodPheromone())
                    return true;

                // Bot Left
                if (positionX < board.height - 1 && board.tiles[positionX + 1][positionY - 1] != null
                        && board.tiles[positionX + 1][positionY - 1].hasFoodPheromone())
                    return true;
            }

            // Top Right
            if (positionX > 0 && board.tiles[positionX - 1][positionY] != null
                    && board.tiles[positionX - 1][positionY].hasFoodPheromone())
                return true;

            // Bot Right
            if (positionX < board.height - 1 && board.tiles[positionX + 1][positionY] != null
                    && board.tiles[positionX + 1][positionY].hasFoodPheromone())
                return true;
        } else {
            // Top Left
            if (positionX > 0 && board.tiles[positionX - 1][positionY] != null
                    && board.tiles[positionX - 1][positionY].hasFoodPheromone())
                return true;

            // Bot Left
            if (positionX < board.height && board.tiles[positionX + 1][positionY] != null
                    && board.tiles[positionX + 1][positionY].hasFoodPheromone())
                return true;

            if (positionY < board.width - 1) {
                // Top Right
                if (positionX > 0 && board.tiles[positionX - 1][positionY + 1] != null
                        && board.tiles[positionX - 1][positionY + 1].hasFoodPheromone())
                    return true;

                // Bot Right
                if (positionX < board.height - 1 && board.tiles[positionX + 1][positionY + 1] != null
                        && board.tiles[positionX + 1][positionY + 1].hasFoodPheromone())
                    return true;
            }
        }
        return false;
    }

    // Attempt to eat something
    public void eat() {
        if (hasFood) {
            hunger = 120;
            hasFood = false;
        } else if (foundFood()) {
            pickFood();
            hunger = 120;
            hasFood = false;
        }
    }

    // Check if Ant is at the Nest
    public boolean atNest() {
        return positionX == board.NEST_X && positionY == board.NEST_Y;
    }

    // Pick up food
    public void pickFood() {
        // Precaution
        if (foundFood()) {
            hasFood = true;
            board.tiles[positionX][positionY].takeFood();
        }
    }

    // Put food down
    public void dropFood() {
        if (hasFood) {
            board.tiles[positionX][positionY].putFood();
            hasFood = false;
        }
    }

    // Not going backwards
    public boolean notBackwards(int newX, int newY) {
        return newX != lastX || newY != lastY;
    }

    // Deposit Search Pheromones in current tile
    public void searchPheromone() {
        board.tiles[positionX][positionY].addSearchPheromone();
    }

    // Deposit Food Pheromones in current tile
    public void foodPheromone() {
        board.tiles[positionX][positionY].addFoodPheromone();
    }

    // Deposit Digging Pheromones in current tile
    public void digPheromone() {
        board.tiles[positionX][positionY].addDigPheromone();
    }

    // Move to nest if beside it
    public boolean moveToNest() {

        if (positionX - 1 == board.NEST_X) {
            // Top Left
            if (board.tiles[positionX][positionY].topLeftY() == board.NEST_Y) {
                move(positionX - 1, board.tiles[positionX][positionY].topLeftY());
                return true;
            }

            // Top Right
            if (board.tiles[positionX][positionY].topRightY() == board.NEST_Y) {
                move(positionX - 1, board.tiles[positionX][positionY].topRightY());
                return true;
            }
        }

        if (positionX + 1 == board.NEST_X) {
            // Bot Left
            if (board.tiles[positionX][positionY].botLeftY() == board.NEST_Y) {
                move(positionX + 1, board.tiles[positionX][positionY].botLeftY());
                return true;
            }

            // Bot Right
            if (board.tiles[positionX][positionY].botRightY() == board.NEST_Y) {
                move(positionX + 1, board.tiles[positionX][positionY].botRightY());
                return true;
            }
        }

        if (positionX == board.NEST_X) {
            // Left
            if (positionY - 1 == board.NEST_Y) {
                move(positionX, positionY - 1);
                return true;
            }
            //Right
            if (positionY + 1 == board.NEST_Y) {
                move(positionX, positionY + 1);
                return true;
            }
        }

        return false;
    }

    // Return to the nest, following Search Pheromone
    public void returnToNest() {

        if (moveToNest())
            return;

        // Decision
        int moveX = -1;
        int moveY = -1;

        // Left
        if (notBackwards(positionX, positionY - 1)) {
            if (board.inBounds(positionX, positionY - 1) && board.tiles[positionX][positionY - 1].hasSearchPheromone()) {
                moveX = positionX;
                moveY = positionY - 1;
            }
        }

        // Right
        if (notBackwards(positionX, positionY + 1)) {
            if (board.inBounds(positionX, positionY + 1) && board.tiles[positionX][positionY + 1].hasSearchPheromone()) {
                if (moveX + moveY != -2) {
                    if (board.tiles[moveX][moveY].searchPheromone()
                            < board.tiles[positionX][positionY + 1].searchPheromone()) {
                        moveX = positionX;
                        moveY = positionY + 1;
                    }
                } else {
                    moveX = positionX;
                    moveY = positionY + 1;
                }
            }
        }

        if (positionX % 2 == 0) {
            if (positionY > 0) {
                if (notBackwards(positionX - 1, positionY - 1)) {
                    // Top Left
                    if (board.inBounds(positionX - 1, positionY - 1) && board.tiles[positionX - 1][positionY - 1].hasSearchPheromone()) {
                        if (moveX + moveY != -2) {
                            if (board.tiles[moveX][moveY].searchPheromone()
                                    < board.tiles[positionX - 1][positionY - 1].searchPheromone()) {
                                moveX = positionX - 1;
                                moveY = positionY - 1;
                            }
                        } else {
                            moveX = positionX - 1;
                            moveY = positionY - 1;
                        }
                    }
                }

                if (notBackwards(positionX + 1, positionY - 1)) {
                    // Bot Left
                    if (board.inBounds(positionX + 1, positionY - 1)
                            && board.tiles[positionX + 1][positionY - 1].hasSearchPheromone()) {
                        if (moveX + moveY > -2) {
                            if (board.tiles[moveX][moveY].searchPheromone()
                                    < board.tiles[positionX + 1][positionY - 1].searchPheromone()) {
                                moveX = positionX + 1;
                                moveY = positionY - 1;
                            }
                        } else {
                            moveX = positionX + 1;
                            moveY = positionY - 1;
                        }
                    }
                }
            }

            // Top Right
            if (notBackwards(positionX - 1, positionY)) {
                if (board.inBounds(positionX - 1, positionY)
                        && board.tiles[positionX - 1][positionY].hasSearchPheromone()) {
                    if (moveX + moveY > -2) {
                        if (board.tiles[moveX][moveY].searchPheromone()
                                < board.tiles[positionX - 1][positionY].searchPheromone()) {
                            moveX = positionX - 1;
                            moveY = positionY;
                        }
                    } else {
                        moveX = positionX - 1;
                        moveY = positionY;
                    }
                }
            }

            if (notBackwards(positionX + 1, positionY)) {
                // Bot Right
                if (board.inBounds(positionX + 1, positionY)
                        && board.tiles[positionX + 1][positionY].hasSearchPheromone()) {
                    if (moveX + moveY > -2) {
                        if (board.tiles[moveX][moveY].searchPheromone()
                                < board.tiles[positionX + 1][positionY].searchPheromone()) {
                            moveX = positionX + 1;
                            moveY = positionY;
                        }
                    } else {
                        moveX = positionX + 1;
                        moveY = positionY;
                    }
                }
            }
        } else {
            if (notBackwards(positionX - 1, positionY)) {
                // Top Left
                if (board.inBounds(positionX - 1, positionY)
                        && board.tiles[positionX - 1][positionY].hasSearchPheromone()) {
                    if (moveX + moveY > -2) {
                        if (board.tiles[moveX][moveY].searchPheromone()
                                < board.tiles[positionX - 1][positionY].searchPheromone()) {
                            moveX = positionX - 1;
                            moveY = positionY;
                        }
                    } else {
                        moveX = positionX - 1;
                        moveY = positionY;
                    }
                }
            }

            if (notBackwards(positionX + 1, positionY)) {
                // Bot Left
                if (board.inBounds(positionX + 1, positionY)
                        && board.tiles[positionX + 1][positionY].hasSearchPheromone()) {
                    if (moveX + moveY > -2) {
                        if (board.tiles[moveX][moveY].searchPheromone()
                                < board.tiles[positionX + 1][positionY].searchPheromone()) {
                            moveX = positionX + 1;
                            moveY = positionY;
                        }
                    } else {
                        moveX = positionX + 1;
                        moveY = positionY;
                    }
                }
            }
            if (positionY < board.width - 1) {
                if (notBackwards(positionX - 1, positionY + 1)) {
                    // Top Right
                    if (board.inBounds(positionX - 1, positionY + 1)
                            && board.tiles[positionX - 1][positionY + 1].hasSearchPheromone()) {
                        if (moveX + moveY > -2) {
                            if (board.tiles[moveX][moveY].searchPheromone()
                                    < board.tiles[positionX - 1][positionY + 1].searchPheromone()) {
                                moveX = positionX - 1;
                                moveY = positionY + 1;
                            }
                        } else {
                            moveX = positionX - 1;
                            moveY = positionY + 1;
                        }
                    }
                }

                if (notBackwards(positionX + 1, positionY + 1)) {
                    // Bot Right
                    if (board.inBounds(positionX + 1, positionY + 1)
                            && board.tiles[positionX + 1][positionY + 1].hasSearchPheromone()) {
                        if (moveX + moveY > -2) {
                            if (board.tiles[moveX][moveY].searchPheromone()
                                    < board.tiles[positionX + 1][positionY + 1].searchPheromone()) {
                                moveX = positionX + 1;
                                moveY = positionY + 1;
                            }
                        } else {
                            moveX = positionX + 1;
                            moveY = positionY + 1;
                        }
                    }
                }
            }
        }

        if (moveX + moveY != -2)
            move(moveX, moveY);
        else
            wander();
    }

    private boolean validMove(int x, int y) {
        if (board.inBounds(x, y)) {
            if (board.tiles[x][y].dug()) {
                move(x, y);
                return true;
            }
        }
        return false;
    }

    // Move to last location3D
    public void moveLast() {
        move(lastX, lastY);
    }

    // Try move left
    public boolean moveLeft() {
        return validMove(positionX, positionY - 1);
    }

    // Try move right
    public boolean moveRight() {
        return validMove(positionX, positionY + 1);
    }

    // Try move top left
    public boolean moveTopLeft() {
        // Even row
        if (positionX % 2 == 0) {
            return validMove(positionX - 1, positionY - 1);
        } else {   // Odd row
            return validMove(positionX - 1, positionY);
        }
    }

    // Try move top right
    public boolean moveTopRight() {
        // Even row
        if (positionX % 2 == 0) {
            return validMove(positionX - 1, positionY);
        } else { // Odd row
            return validMove(positionX - 1, positionY + 1);
        }
    }

    // Try move bot left
    public boolean moveBotLeft() {
        if (positionX % 2 == 0) {
            return validMove(positionX + 1, positionY - 1);
        } else {
            return validMove(positionX + 1, positionY);
        }
    }

    // Try move bot right
    public boolean moveBotRight() {
        // Botright
        if (positionX % 2 == 0) {
            return validMove(positionX + 1, positionY);
        } else {
            return validMove(positionX + 1, positionY + 1);
        }
    }
}
