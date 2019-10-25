#!/usr/bin/env bash

set -euxo pipefail

exec java -cp /app/hackathon-blockchain-explorer.jar:/app/resources co.ledger.be.App
