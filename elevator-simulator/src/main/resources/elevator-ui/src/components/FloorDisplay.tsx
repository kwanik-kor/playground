import { ChevronUp, ChevronDown } from 'lucide-react';

interface FloorDisplayProps {
  floor: number;
  direction: 'up' | 'down' | null;
  isMoving: boolean;
}

export function FloorDisplay({ floor, direction, isMoving }: FloorDisplayProps) {
  return (
    <div className="relative">
      {/* Display panel housing - brushed metal */}
      <div className="bg-gradient-to-br from-[#b8b8b8] to-[#989898] border-2 border-[#808080] rounded px-8 py-4 shadow-lg">
        {/* Screen inset */}
        <div className="bg-black rounded-sm p-6 shadow-inner-display">
          <div className="flex items-center justify-center gap-3 min-w-[160px]">
            {/* Direction indicator */}
            {isMoving && direction && (
              <div className="direction-indicator">
                {direction === 'up' ? (
                  <ChevronUp className="w-8 h-8 text-[#00ff00]" strokeWidth={2.5} />
                ) : (
                  <ChevronDown className="w-8 h-8 text-[#00ff00]" strokeWidth={2.5} />
                )}
              </div>
            )}
            
            {/* Floor number - LED style */}
            <div className="floor-number">
              {floor}
            </div>
          </div>
        </div>

        {/* Brushed metal texture on panel */}
        <div className="absolute inset-0 pointer-events-none rounded brushed-panel" />
      </div>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@700&display=swap');

        .floor-number {
          font-family: 'Orbitron', monospace;
          font-size: 3.5rem;
          line-height: 1;
          color: #00ff00;
          text-shadow: 
            0 0 15px rgba(0, 255, 0, 0.9),
            0 0 30px rgba(0, 255, 0, 0.6),
            0 0 45px rgba(0, 255, 0, 0.3);
          font-weight: 700;
        }

        .direction-indicator {
          filter: drop-shadow(0 0 10px rgba(0, 255, 0, 0.7));
        }

        .shadow-inner-display {
          box-shadow: 
            inset 0 2px 8px rgba(0, 0, 0, 0.8),
            inset 0 0 20px rgba(0, 0, 0, 0.5);
        }

        .brushed-panel {
          background-image: repeating-linear-gradient(
            90deg,
            transparent,
            rgba(255, 255, 255, 0.05) 1px,
            transparent 2px
          );
        }
      `}</style>
    </div>
  );
}