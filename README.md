Hackathon Blockchain Explorer

is based on Tresor blockbook to provide a backup to
Ledger Blochain Explorer.

It is implemented for Litecoin

Some implemented endpoints:



- /blockchain/v3/transaction/<txid>
- /blockchain/v3/address/<address>
- /blockchain/v3/blocks/<blockheight>

To run it on top of docker:

docker build . t hackathon-blockchain-explorer

docker run hackathon-blockchain-explorer