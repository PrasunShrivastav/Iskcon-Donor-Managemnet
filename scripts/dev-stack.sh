#!/bin/sh
set -eu

ROOT_DIR=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
LOG_DIR="$ROOT_DIR/.codex-logs"
mkdir -p "$LOG_DIR"

PIDS=""

cleanup() {
  for pid in $PIDS; do
    kill "$pid" 2>/dev/null || true
  done
  wait 2>/dev/null || true
}

trap cleanup INT TERM EXIT

start_service() {
  name="$1"
  port="$2"
  command="$3"
  log_file="$LOG_DIR/$name.log"

  if lsof -nP -iTCP:"$port" -sTCP:LISTEN >/dev/null 2>&1; then
    printf '%s already appears to be running on port %s\n' "$name" "$port"
    return
  fi

  (
    cd "$ROOT_DIR"
    sh -lc "$command"
  ) >"$log_file" 2>&1 &

  pid=$!
  PIDS="$PIDS $pid"
  printf '%s started on pid %s\n' "$name" "$pid"
  printf '  log: %s\n' "$log_file"
}

start_service "backend" "8080" "cd ISKCON-GEV-main && ./mvnw -q -DskipTests spring-boot:run"
start_service "admin" "5173" "cd GEV-ADMIN-main && npm run dev -- --host 0.0.0.0 --port 5173"
start_service "public" "5500" "python3 -m http.server 5500 --directory ISKCON-GEV-FRONTEND-main"

cat <<EOF

Local stack is starting:
  Backend: http://localhost:8080
  Admin:   http://localhost:5173
  Public:  http://localhost:5500/donation_landingpage.html

Press Ctrl+C to stop all services.
EOF

wait
