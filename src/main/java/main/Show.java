package main;

import processing.core.PApplet;

/**
 * @classname: simpleProcessing
 * @description:
 * @author: amomorning
 * @date: 2020/06/16
 */
public class Show extends PApplet {
    // Window size
    int N = 600;

    // Circle without diffusion
    int[][] pt = new int[][]{{200, 360},  {400, 200}, {400, 500}};
    int[] r = new int[]{50, 30, 60};

    //System parameters
    double diffU;
    double diffV;
    double paramF;
    double paramK;

    boolean rndInitCondition;

    double[][] U = new double[N][N];
    double[][] V = new double[N][N];

    double[][] dU = new double[N][N];
    double[][] dV = new double[N][N];

    int[][] offset = new int[N][2];


    void generateInitialState() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                U[i][j] = 1.0;
                V[i][j] = 0.0;
            }
        }

        if (rndInitCondition) {
            for (int i = 2*N/5; i < 3*N/5; i++) {
                for (int j = 2*N/5; j < 3*N/5; j++) {

                    if (checkCircle(i, j)) continue;

                    U[i][j] = 0.5*(1 + random(-1, 1));
                    V[i][j] = 0.25*( 1 + random(-1, 1));
                }
            }
        } else {
            System.out.println("Fixed uv");
            for (int i = N/3; i < 2*N/3; i++) {
                for (int j = N/3; j < 2*N/3; j++) {
                    U[i][j] = 0.5;
                    V[i][j] = 0.25;
                }
            }
        }
    }

    public void settings() {
        size(N,N, P2D);

    }

    public void setup() {

        frameRate(25);
        smooth();
        colorMode(HSB,1.0f);
        //Set default parameters;
        diffU = 0.16;
        diffV = 0.08;
        paramF = 0.03;
        paramK = 0.06;

        rndInitCondition = true;

        //Populate U and V with initial data
        generateInitialState();

        //Set up offsets
        for (int i = 1; i < N-1; i++) {
            offset[i][0] = i-1;
            offset[i][1] = i+1;
        }

        offset[0][0] = N-1;
        offset[0][1] = 1;

        offset[N-1][0] = N-2;
        offset[N-1][1] = 0;
    }

    public void timestep(double F, double K, double diffU, double diffV) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                double u = U[i][j];
                double v = V[i][j];

                int left = offset[i][0];
                int right = offset[i][1];
                int up = offset[j][0];
                int down = offset[j][1];

                double uvv = u*v*v;

                double lapU = (U[left][j] + U[right][j] + U[i][up] + U[i][down] - 4*u);
                double lapV = (V[left][j] + V[right][j] + V[i][up] + V[i][down] - 4*v);

                dU[i][j] = diffU*lapU - uvv + F*(1 - u);
                dV[i][j] = diffV*lapV + uvv - (K+F)*v;
            }
        }


        for (int i= 0; i < N; i++) {
            for (int j = 0; j < N; j++){
                if (checkCircle(i, j)) continue;
                U[i][j] += dU[i][j];
                V[i][j] += dV[i][j];
            }
        }


    }

    // Check if inside circle
    private boolean checkCircle(int x, int y) {
        for(int i = 0; i < pt.length; ++ i) {
            if((x-pt[i][0])*(x-pt[i][0]) + (y-pt[i][1])*(y-pt[i][1]) < r[i] * r[i]) {
                return true;
            }
        }
        return false;
    }

    public void draw(){
        for (int k = 0; k < 5; k++) {
            timestep(paramF, paramK, diffU, diffV);
        }

        // Draw points
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                set(i, j, color((float)(1-U[i][j])));
            }
        }
    }


    public void keyPressed() {
        switch (key) {
            case '1':
                diffU = 0.16;
                diffV = 0.08;
                paramF = 0.035;
                paramK = 0.06;
                generateInitialState();
                break;
            case '2':
                diffU = 0.16;
                diffV = 0.08;
                paramF = 0.042;
                paramK = 0.065;
                generateInitialState();
                break;
            case '3':
                diffU = 0.18;
                diffV = 0.13;
                paramF = 0.025;
                paramK = 0.056;
                generateInitialState();
                break;
            case '4':
                diffU = 0.18;
                diffV = 0.09;
                paramF = 0.02;
                paramK = 0.056;
                generateInitialState();
                break;
            case '5':
                diffU = 0.14;
                diffV = 0.06;
                paramF = 0.035;
                paramK = 0.065;
                generateInitialState();
                break;
            case '6':
                diffU = 0.19;
                diffV = 0.09;
                paramF = 0.062;
                paramK = 0.062;
                generateInitialState();
                break;
            case '7':
                diffU = 0.16;
                diffV = 0.08;
                paramF = 0.05;
                paramK = 0.065;
                generateInitialState();
                break;
            case 'r':
                rndInitCondition = true;
                generateInitialState();
                break;
            case 'n':
                rndInitCondition = false;
                generateInitialState();
        }
    }
}
