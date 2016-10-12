import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt(); // number of rows.
        int C = in.nextInt(); // number of columns.
        int A = in.nextInt(); // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.

        Node[][] fields = new Node[R][C];

        for (int row = 0; row < R; row++) {
            for (int column = 0; column < C; column++) {
                fields[column][row] = new Node();
            }
        }

        for (int row = 0; row < R; row++) {
            for (int column = 0; column < C; column++) {
                fields[column][row].addNeighbour(Direction.UP, Direction.UP.of(fields, column, row));
                fields[column][row].addNeighbour(Direction.DOWN, Direction.DOWN.of(fields, column, row));
                fields[column][row].addNeighbour(Direction.LEFT, Direction.LEFT.of(fields, column, row));
                fields[column][row].addNeighbour(Direction.RIGHT, Direction.RIGHT.of(fields, column, row));
            }
        }


        // game loop
        while (true) {
            int KR = in.nextInt(); // row where Kirk is located.
            int KC = in.nextInt(); // column where Kirk is located.
            for (int row = 0; row < R; row++) {
                String ROW = in.next(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).

                for (int col = 0; col < C; col++) {
                    fields[col][row].type = NodeType.byRepresentation(ROW.charAt(col));
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("RIGHT"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        }
    }

    enum NodeType {
        WALL('#'),
        START_POSITION('T'),
        END_POSITION('C'),
        UNDEFINED('?'),
        SPACE('.');

        char representation;

        NodeType(char representation) {
            this.representation = representation;
        }

        static NodeType byRepresentation(char representation) {
            return Arrays.stream(NodeType.values())
                    .filter(nt -> nt.representation == representation)
                    .findAny()
                    .orElse(null);
        }
    }

    enum Direction {
        UP(identity(), y -> y - 1),
        DOWN(identity(), y -> y + 1),
        LEFT(x -> x - 1, identity()),
        RIGHT(x -> x + 1, identity());

        Function<Integer, Integer> neighbourRowPosition;
        Function<Integer, Integer> neighbourColumnPosition;

        Direction(Function<Integer, Integer> neighbourRowPosition, Function<Integer, Integer> neighbourColumnPosition) {
            this.neighbourRowPosition = neighbourRowPosition;
            this.neighbourColumnPosition = neighbourColumnPosition;
        }

        Node of(Node[][] array, int x, int y) {
            int x1 = neighbourColumnPosition.apply(x);
            int y1 = neighbourRowPosition.apply(y);
            if (x1 >= 0 && x1 < array.length) {
                if (y1 >= 0 && y1 < array[0].length) {
                    return array[x1][y1];
                }
            }
            return null;
        }
    }


    static class Node {

        NodeType type = NodeType.UNDEFINED;
        Integer stepsToStart = Integer.MAX_VALUE;

        Map<Direction, Node> neighbours = new HashMap<>();

        void addNeighbour(Direction direction, Node node) {
            if (nonNull(node)) {
                addNeighbour(direction, node);
            }
        }

        void updateDistance() {
            switch (type) {
                case START_POSITION:
                    stepsToStart = 0;
                    break;
                case END_POSITION:
                case SPACE:
                    stepsToStart = neighbours.values().stream().map(Node::getStepsToStart).min(Integer::compare).get() + 1;
                    neighbours.values().stream().filter(n -> n.stepsToStart > ).forEach(Node::updateDistance);
                    break;
                default:

            }
        }

        public Integer getStepsToStart() {
            return stepsToStart;
        }
    }

}