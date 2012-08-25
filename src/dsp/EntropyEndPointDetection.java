package dsp;

import java.util.List;

/**
 * @author niktrk
 * 
 */
public class EntropyEndPointDetection extends ThresholdEndPointDetection {

	private static final double OFFSET_FACTOR = 1d;

	public EntropyEndPointDetection(List<Frame> frames) {
		super(frames, false);
	}

	@Override
	protected double calculateFrameValue(Frame frame) {
		// total energy
		double totalSpectralEnergy = 0d;
		for (int i = 0; i < frame.getBuffer().length / 2; i++) {
			// if (i * Constants.FREQUENCY_STEP > 350 && i *
			// Constants.FREQUENCY_STEP < 3750) {
			totalSpectralEnergy += frame.getBuffer()[i];
			// }
		}
		// probability
		double[] probabilityDensity = new double[frame.getBuffer().length / 2];
		for (int i = 0; i < probabilityDensity.length; i++) {
			if (Math.abs(totalSpectralEnergy) < Constants.EPS)
			// || (i * Constants. FREQUENCY_STEP <= 350 || i *
			// Constants.FREQUENCY_STEP >= 3750))
			{
				probabilityDensity[i] = 0d;
			} else {
				probabilityDensity[i] = frame.getBuffer()[i] / totalSpectralEnergy;
			}
		}
		// entropy
		double spectralEntropy = 0d;
		for (int i = 0; i < probabilityDensity.length; i++) {
			if (probabilityDensity[i] > Constants.EPS) {
				spectralEntropy += probabilityDensity[i] * Math.log(probabilityDensity[i]);
			}
		}
		return -spectralEntropy;
	}

	@Override
	protected double calculateThreshold(double min, double max) {
		return (max - min) / 2 + OFFSET_FACTOR * min;
	}

	@Override
	protected int getMinConsecutiveSpeechFrames() {
		return 7;
	}

	@Override
	protected int getEndPointDetectionOffset() {
		return 5;
	}

}
