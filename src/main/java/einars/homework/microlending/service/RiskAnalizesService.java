package einars.homework.microlending.service;

import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import einars.homework.microlending.domain.Loan;

/**
 * Service for risk analize.
 */


@Service
public class RiskAnalizesService {
	
    @Value("${maxAmount}")
    private int maxAmount;

	private final Logger log = LoggerFactory.getLogger(RiskAnalizesService.class);

	public boolean giveLoan(Loan loan) {
		if (LocalTime.now().getHour() < 1 && loan.getAmount()>=maxAmount) {
			log.debug("To give or not to give to this {}, that is the question", loan.getClient().getName());
			return false;
		}
		return true;
	}

}
