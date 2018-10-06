# PedityWallet built on Stellar
Pedity Wallet application for android devices targetting devices >= 26 level API
[Meet Google Play's target API level requirement
](https://developer.android.com/distribute/best-practices/develop/target-sdk "Meet Google Play's target API level requirement
")


| Pedity Wallet        | Version           | 1.0  |
| ------------- |:-------------:| -----:|
| ![Pedity Wallet](https://cdn-images-1.medium.com/max/400/1*-B20q9feS15ZdzCKQs1L2w.jpeg "Splash page")    | ![Pedity loging page](https://cdn-images-1.medium.com/max/400/1*mZhMlNOo8csdutBNlW3Dng.jpeg "Pedity Login Page")| ![Pedity Receive Page](https://cdn-images-1.medium.com/max/400/1*LGlTd5JfNs0SAWucGjz6Lg.jpeg "Pedity Receive page") |

## Pedity Wallet version 1.0
Version 1.0 is a proper release version for Pedity Android Wallet.

### List of Features
1. Dashboard to show Balance of PEDI and XLM
2. Send functionality with Optional Memo Field
3. QR code scanner for scanning destination address
4. Receive page to show receive address with easy to use copy feature
5. Receive page to show QR code address for simplifing payments
6. History to show list of send and received transactions
7. Native RTL support in the app
8. Application targetting API >=26


### Prerequisites
> Android devices with API greater than 26 

> Basically Android Oreo devices

### Version and releases information
> Application release format is XX.YY 

> XX meaning Major update

> YY Meaning Minor update

Source Code will be available once testing is complete i.e. version 1.1 or 1.2 will feature source code.


### BUGS :-
    [FIXED] UI issue - After sending is complete the User is unable to view the success message and it should not show the same send page after that.
    [FIXED] UI - Change text on bottom of scan screen
    [FIXED] Bug on Send confirmation page
    [FIXED] Addition on Toolbar on send page

### FEATURE Addition
    [FIXED] Key not getting stored on device

### LIMITATIONS :-
    [FIXED] History Page reloading issue and fix stream method
    [FIXED] History does not differenciate between receive and sent transactions available - WIP
    [FIXED] Optimizations related to balance fees deduction


### Next List of features 
- Speed optimizations
- Addition of features related to PEDI platform
- Memory Optimizations
- Trustline and valid address check is not present
- Only MEMO text supported, in future memo id, memo hash and memo text needs to be added
- Ledger Nano S Support in v1.2

### ISSUE
In case of any issues, please raise an issue on github.
