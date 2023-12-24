package dto;

import lombok.*;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
@Getter
public class CustomerDto {
    private String id;
    private String name;
    private String address;
    private double salary;

}
