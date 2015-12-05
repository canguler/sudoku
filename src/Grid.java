import java.util.Arrays;
import java.util.List;

/**
 * Created by can on 03.12.2015.
 */
public class Grid {
    private int length;
    private int gridSize;
    private int[][] grid;
    private String seperatorLineString;

    public Grid(int gridSize) {
        this.gridSize = gridSize;
        this.length = gridSize * gridSize;
        this.grid = new int[this.length][this.length];
        this.seperatorLineString = computeSeperatorLineString();
    }

    public boolean isComplete() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isRowValid(int rowIndex) {
        boolean[] isNumberOccured = new boolean[10];
        for (int i = 0; i < length; i++) {
            int num = grid[rowIndex][i];
            if (num != 0) {
                if (isNumberOccured[num]) {
                    return false;
                } else {
                    isNumberOccured[num] = true;
                }
            }
        }
        return true;
    }

    private boolean isColumnValid(int columnIndex) {
        boolean[] isNumberOccured = new boolean[10];
        for (int i = 0; i < length; i++) {
            int num = grid[i][columnIndex];
            if (num != 0) {
                if (isNumberOccured[num]) {
                    return false;
                } else {
                    isNumberOccured[num] = true;
                }
            }
        }
        return true;
    }

    private boolean isSubGridValid(int subGridIndex) {
        boolean[] isNumberOccured = new boolean[10];
        for (int i = subGridIndex / gridSize * gridSize;
             i < subGridIndex / gridSize * gridSize + gridSize;
             i++) {
            for (int j = subGridIndex % gridSize * gridSize;
                 j < subGridIndex % gridSize * gridSize + gridSize;
                 j++) {
                int num = grid[i][j];
                if (num != 0) {
                    if (isNumberOccured[num]) {
                        return false;
                    } else {
                        isNumberOccured[num] = true;
                    }
                }
            }
        }
        return true;
    }

    public boolean isValid() {
        for (int i = 0; i < length; i++) {
            if (!isRowValid(i)) {
                return false;
            }
        }

        for (int i = 0; i < length; i++) {
            if (!isColumnValid(i)) {
                return false;
            }
        }

        for (int i = 0; i < length; i++) {
            if (!isSubGridValid(i)) {
                return false;
            }
        }

        return true;
    }

    private String computeLineString(int lineIndex) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append('|');
        for (int j = 0; j < gridSize; j++) {
            for (int k = 0; k < gridSize; k++) {
                int cell = grid[lineIndex][j * gridSize + k];
                if (cell == 0) {
                    lineBuilder.append('.');
                } else {
                    lineBuilder.append(grid[lineIndex][j * gridSize + k]);
                }
            }
            lineBuilder.append('|');
        }
        lineBuilder.append('\n');

        return lineBuilder.toString();
    }

    public String computeSeperatorLineString() {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append('+');
        for (int j = 0; j < gridSize; j++) {
            for (int k = 0; k < gridSize; k++) {
                lineBuilder.append('-');
            }
            lineBuilder.append('+');
        }
        lineBuilder.append('\n');

        return lineBuilder.toString();
    }

    public boolean[][][] computeIsCandidate() {
        boolean[][][] result = new boolean[length][length][length+1];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == 0) {
                    for (int candidate = 1; candidate <= length; candidate++) {
                        if (rowContains(i, candidate) ||
                                columnContains(j, candidate) ||
                                subGridContains(i / gridSize * gridSize + j / gridSize, candidate)) {
                            result[i][j][candidate] = false;
                        } else {
                            result[i][j][candidate] = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean solve() {
        if (!isValid()) {
            return false;
        }

        if (isComplete()) {
            return true;
        }

        boolean[][][] isCandidate = computeIsCandidate();
        int minCandidateCount = length + 1;
        int bestRowIndex = -1;
        int bestColumnIndex = -1;

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == 0) {
                    int candidateCount = 0;
                    for (int candidate = 1; candidate <= length; candidate++) {
                        if (isCandidate[i][j][candidate]) {
                            candidateCount++;
                        }
                    }
                    if (candidateCount < minCandidateCount) {
                        minCandidateCount = candidateCount;
                        bestRowIndex = i;
                        bestColumnIndex = j;
                    }
                }
            }
        }

        for (int candidate = 1; candidate <= length; candidate++) {
            if (isCandidate[bestRowIndex][bestColumnIndex][candidate]) {
                setCell(bestRowIndex, bestColumnIndex, candidate);
                if (solve()) {
                    return true;
                }
                setCell(bestRowIndex, bestColumnIndex, 0);
            }
        }

        return false;
    }

    public void setCell(int rowIndex, int columnIndex, int number) {
        grid[rowIndex][columnIndex] = number;
    }

    public int getCell(int rowIndex, int columnIndex) {
        return grid[rowIndex][columnIndex];
    }

    public boolean rowContains(int rowIndex, int candidate) {
        for (int i = 0; i < length; i++) {
            if (grid[rowIndex][i] == candidate) {
                return true;
            }
        }
        return false;
    }

    public boolean columnContains(int columnIndex, int candidate) {
        for (int i = 0; i < length; i++) {
            if (grid[i][columnIndex] == candidate) {
                return true;
            }
        }
        return false;
    }

    public boolean subGridContains(int subGridIndex, int candidate) {
        for (int i = subGridIndex / gridSize * gridSize;
             i < subGridIndex / gridSize * gridSize + gridSize; i++) {
            for (int j = subGridIndex % gridSize * gridSize;
                 j < subGridIndex % gridSize * gridSize + gridSize;
                 j++) {
                if (grid[i][j] == candidate) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append(seperatorLineString);
        for (int h = 0; h < gridSize; h++) {
            for (int i = 0; i < gridSize; i++) {
                resultBuilder.append(computeLineString(h * gridSize + i));
            }
            resultBuilder.append(seperatorLineString);
        }
        return resultBuilder.toString();
    }

    public void fillGrid(String gridString) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                grid[i][j] = gridString.charAt(i * length + j) - '0';
            }
        }
    }
}