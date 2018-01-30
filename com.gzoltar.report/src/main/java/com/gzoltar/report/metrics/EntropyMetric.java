package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public class EntropyMetric extends AbstractMetric {

  private final IFormula formula;

  public EntropyMetric(final IFormula formula) {
    this.formula = formula;
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    double entropy = 0.0;
    for (Node node : spectrum.getTargetNodes()) {
      double suspiciousness = node.getSuspiciousnessValue(this.formula.getName());
      if (Double.compare(suspiciousness, 0.0) > 0) {
        entropy += suspiciousness * this.log2(suspiciousness);
      }
    }

    return -1.0 * entropy;
  }

  @Override
  public String getName() {
    return "Entropy_" + this.formula.getName();
  }
}
