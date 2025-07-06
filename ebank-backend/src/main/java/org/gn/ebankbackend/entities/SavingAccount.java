package org.gn.ebankbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
