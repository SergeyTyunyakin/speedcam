package ru.speedcam.transfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.speedcam.models.User;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String regionCode;

    public static UserDto from(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .regionCode(user.getRegionCode())
                .build();
    }
}