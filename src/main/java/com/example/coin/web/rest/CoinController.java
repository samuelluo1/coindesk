package com.example.coin.web.rest;

import com.example.coin.model.CoinChineseConversion;
import com.example.coin.model.CoinRateVO;
import com.example.coin.service.CoinService;
import com.example.coin.web.rest.errors.BadRequestAlertException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


/**
 * @author Sameul Luo
 */
@RestController
@RequestMapping(path = "/api/v1/coin")
public class CoinController {

	private final CoinService coinService;

	public CoinController(CoinService coinService) {
		this.coinService = coinService;
	}

	/**
	 * {@code GET  /conversion/chinese/:code} : get the "code" CoinChineseConversion.
	 *
	 * @param code the code of the CoinChineseConversion to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the CoinChineseConversion,
	 * or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/conversion/chinese/{code}")
	public ResponseEntity<CoinChineseConversion> getChinese(@PathVariable String code) {
		Optional<CoinChineseConversion> result = coinService.findByCode(code);
		return result.map(ResponseEntity::ok)
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * {@code POST  /conversion/chinese} : Create a new CoinChineseConversion.
	 *
	 * @param coinChineseConversion the CoinChineseConversion to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new department,
	 * or with status {@code 400 (Bad Request)} if the department has already an ID.
	 */
	@PostMapping("/conversion/chinese")
	public ResponseEntity<CoinChineseConversion> createChinese(@Valid @RequestBody CoinChineseConversion coinChineseConversion) {
		if (coinChineseConversion.getId() != null) {
			throw new BadRequestAlertException("Cannot create by specify ID");
		}
		CoinChineseConversion result = coinService.save(coinChineseConversion);
		return ResponseEntity.ok().body(result);
	}

	/**
	 * {@code PUT  /conversion/chinese} : Updates an existing CoinChineseConversion.
	 *
	 * @param coinChineseConversion the CoinChineseConversion to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated department,
	 * or with status {@code 400 (Bad Request)} if the CoinChineseConversion is not valid,
	 * or with status {@code 500 (Internal Server Error)} if the CoinChineseConversion couldn't be updated.
	 */
	@PutMapping("/conversion/chinese")
	public ResponseEntity<CoinChineseConversion> updateChinese(@Valid @RequestBody CoinChineseConversion coinChineseConversion) {
		if (coinChineseConversion.getId() == null) {
			throw new BadRequestAlertException("Null id");
		}
		CoinChineseConversion result = coinService.save(coinChineseConversion);
		return ResponseEntity.ok().body(result);
	}

	/**
	 * {@code DELETE  /conversion/chinese/:id} : delete the "id" CoinChineseConversion.
	 *
	 * @param id the id of the CoinChineseConversion to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/conversion/chinese/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
		coinService.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * {@code GET  /rate} : get the coin rate after transforming.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the CoinRateVO,
	 */
	@GetMapping("/rate")
	public ResponseEntity<CoinRateVO> getCoinRate() {
		CoinRateVO result = coinService.getCoinRate();
		return ResponseEntity.ok().body(result);
	}
}
