-- Updating the email instituitional noreply email adress 
UPDATE SENDER
SET FROM_ADDRESS='noreply@tecnico.ulisboa.pt'
WHERE FROM_ADDRESS='noreply@ist.utl.pt';

