package ca.bc.gov.educ.api.digitalid.service;

import ca.bc.gov.educ.api.digitalid.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.digitalid.exception.InvalidParameterException;
import ca.bc.gov.educ.api.digitalid.model.DigitalIDEntity;
import ca.bc.gov.educ.api.digitalid.repository.DigitalIDRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * DigitalIDService
 *
 * @author John Cox
 */

@Service
public class DigitalIDService {
  private static final String DIGITAL_ID_ATTRIBUTE = "digitalID";

  @Getter(AccessLevel.PRIVATE)
  private final DigitalIDRepository digitalIDRepository;

  DigitalIDService(@Autowired final DigitalIDRepository digitalIDRepository) {
    this.digitalIDRepository = digitalIDRepository;
  }

  /**
   * Search for DigitalIDEntity by identity value and identity type code (BCeID or BCSC)
   *
   * @param typeValue
   * @param typeCode
   * @return
   * @throws EntityNotFoundException
   */
  public DigitalIDEntity searchDigitalId(String typeCode, String typeValue) {

    Optional<DigitalIDEntity> result = getDigitalIDRepository().findByIdentityTypeCodeAndIdentityValue(typeCode.toUpperCase(), typeValue.toUpperCase());

    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(DigitalIDEntity.class, "identityTypeCode", typeCode, "identityTypeValue", typeValue);
    }
  }

  /**
   * Search for DigitalIDEntity by digital id
   *
   * @param id
   * @return
   * @throws EntityNotFoundException
   */
  public DigitalIDEntity retrieveDigitalID(UUID id) {
    Optional<DigitalIDEntity> result = getDigitalIDRepository().findById(id);
    if (result.isPresent()) {
      return result.get();
    } else {
      throw new EntityNotFoundException(DigitalIDEntity.class, DIGITAL_ID_ATTRIBUTE, id.toString());
    }
  }

  /**
   * Creates a DigitalIDEntity
   *
   * @param digitalID
   * @return
   * @throws EntityNotFoundException
   * @throws InvalidParameterException
   */
  public DigitalIDEntity createDigitalID(DigitalIDEntity digitalID) {

    validateCreateParameters(digitalID);

    if (digitalID.getDigitalID() != null) {
      throw new InvalidParameterException(DIGITAL_ID_ATTRIBUTE);
    }
    digitalID.setUpdateDate(new Date());
    digitalID.setCreateDate(new Date());

    return digitalIDRepository.save(digitalID);
  }

  /**
   * Updates a DigitalIDEntity
   *
   * @param digitalID
   * @return
   * @throws Exception
   */
  public DigitalIDEntity updateDigitalID(DigitalIDEntity digitalID) {

    validateCreateParameters(digitalID);

    Optional<DigitalIDEntity> curDigitalID = digitalIDRepository.findById(digitalID.getDigitalID());

    if (curDigitalID.isPresent()) {
      DigitalIDEntity newDigitalID = curDigitalID.get();
      newDigitalID.setStudentID(digitalID.getStudentID());
      newDigitalID.setIdentityTypeCode(digitalID.getIdentityTypeCode());
      newDigitalID.setIdentityValue(digitalID.getIdentityValue());
      newDigitalID.setLastAccessDate(digitalID.getLastAccessDate());
      newDigitalID.setLastAccessChannelCode(digitalID.getLastAccessChannelCode());
      newDigitalID.setUpdateDate(new Date());
      newDigitalID = digitalIDRepository.save(newDigitalID);

      return newDigitalID;
    } else {
      throw new EntityNotFoundException(DigitalIDEntity.class, DIGITAL_ID_ATTRIBUTE, digitalID.getDigitalID().toString());
    }
  }

  private void validateCreateParameters(DigitalIDEntity digitalIDEntity) {
    if (digitalIDEntity.getCreateDate() != null)
      throw new InvalidParameterException("createDate");
    if (digitalIDEntity.getUpdateDate() != null)
      throw new InvalidParameterException("updateDate");
  }

}
