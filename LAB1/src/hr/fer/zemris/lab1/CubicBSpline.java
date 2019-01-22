package hr.fer.zemris.lab1;

import java.util.List;

import hr.fer.zemris.linearna.IMatrix;
import hr.fer.zemris.linearna.IVector;
import hr.fer.zemris.linearna.Matrix;
import hr.fer.zemris.linearna.Vector;

public class CubicBSpline {
	
	private static final Matrix MATRIX_B = Matrix.parseSimple("-1 3 -3 1|3 -6 3 0|-3 0 3 0|1 4 1 0");
	
	private List<Vector> controlPoints;
	
	public CubicBSpline(List<Vector> controlPoints) {
		if(controlPoints == null) {
			throw new IllegalArgumentException("Argument must not be null.");
		}
		this.controlPoints = controlPoints;
	}
	
	public void addControlPoint(int index, Vector point) {
		controlPoints.add(index, point);
	}
	
	public void addControlPoint(Vector point) {
		controlPoints.add(point);
	}
	
	public int getSegmentCount() {
		return Math.max(0, controlPoints.size() - 3);
	}
	
	public IVector point(int segment, double t) {
		Vector vectorT = new Vector(t*t*t, t*t, t, 1);
		
		Matrix matrixR = new Matrix(4, 3);
		for(int i = 0; i < 4; i++) {
			Vector r = controlPoints.get(segment+i);
			for(int j = 0; j < 3; j++) {
				matrixR.set(i, j, r.get(j));
			}
		}
		
		IMatrix m = vectorT.scalarMultiply(1.0/6).toRowMatrix(true).nMultiply(MATRIX_B).nMultiply(matrixR);
		return m.toVector(true);
	}
	
	public IVector tangent(int segment, double t) {
		Vector vectorT = new Vector(3*t*t, 2*t, 1, 0);
		
		Matrix matrixR = new Matrix(4, 3);
		for(int i = 0; i < 4; i++) {
			Vector r = controlPoints.get(segment+i);
			for(int j = 0; j < 3; j++) {
				matrixR.set(i, j, r.get(j));
			}
		}
		
		IMatrix m = vectorT.scalarMultiply(1.0/6).toRowMatrix(false).nMultiply(MATRIX_B).nMultiply(matrixR);
		return m.toVector(true).normalize();
	}
}
