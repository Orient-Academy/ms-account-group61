package az.edu.orient.mapper;

import az.edu.orient.constant.ActiveStatusConstant;
import az.edu.orient.constant.ExceptionMessageConstant;
import az.edu.orient.dto.AccountDto;
import az.edu.orient.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "activeStatus", expression = "java(accountDto.getActiveStatus().getValue())")
    Account mapAccount(AccountDto accountDto);

    @Mapping(target = "activeStatus", expression = "java(mapActiveStatus(account.getActiveStatus()))")
    AccountDto mapAccount(Account account);

    default ActiveStatusConstant mapActiveStatus(byte value) {
        for (ActiveStatusConstant activeStatus : ActiveStatusConstant.values()) {
            if (activeStatus.getValue() == value) {
                return activeStatus;
            }
        }
        throw new IllegalArgumentException(ExceptionMessageConstant.INCORRECT_ACTIVE_STATUS_CONSTANT_VALUE.getMessage());
    }
}
